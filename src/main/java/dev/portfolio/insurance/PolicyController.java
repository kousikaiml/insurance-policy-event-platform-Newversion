package dev.portfolio.insurance;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {
    private final PolicyService service;
    private final PolicyRepository repository;
    public PolicyController(PolicyService service, PolicyRepository repository) {
        this.service = service; this.repository = repository;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Policy create(@Valid @RequestBody PolicyRequest request) { return service.create(request); }

    @GetMapping("/{policyNumber}")
    public Policy get(@PathVariable String policyNumber) {
        return repository.findByPolicyNumber(policyNumber)
            .orElseThrow(() -> new PolicyNotFoundException(policyNumber));
    }

    @ExceptionHandler(PolicyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFound(PolicyNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> badRequest(IllegalArgumentException ex) {
        return Map.of("error", ex.getMessage());
    }
}
