package dev.portfolio.insurance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Service
public class PolicyService {
    private final PolicyRepository policies;
    private final OutboxEventRepository outbox;
    private final ObjectMapper mapper;

    public PolicyService(PolicyRepository policies, OutboxEventRepository outbox, ObjectMapper mapper) {
        this.policies = policies; this.outbox = outbox; this.mapper = mapper;
    }

    @Transactional
    public Policy create(PolicyRequest request) {
        if (request.expirationDate().isBefore(request.effectiveDate())) {
            throw new IllegalArgumentException("Expiration date must not be before effective date");
        }
        if (policies.findByPolicyNumber(request.policyNumber()).isPresent()) {
            throw new IllegalArgumentException("Policy number already exists");
        }
        Policy policy = policies.save(new Policy(request.policyNumber(), request.insuredName(),
            request.lob(), request.premium(), request.effectiveDate(), request.expirationDate()));
        try {
            String payload = mapper.writeValueAsString(Map.of(
                "eventType", "POLICY_CREATED", "policyNumber", policy.getPolicyNumber(),
                "insuredName", policy.getInsuredName(), "lob", policy.getLob(),
                "premium", policy.getPremium(), "effectiveDate", policy.getEffectiveDate(),
                "expirationDate", policy.getExpirationDate()));
            outbox.save(new OutboxEvent(policy.getPolicyNumber(), "POLICY_CREATED", payload));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not serialize policy event", e);
        }
        return policy;
    }
}
