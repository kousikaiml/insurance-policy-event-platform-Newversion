package dev.portfolio.insurance;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PolicyRequestTest {
    @Test void requestCarriesExpectedPolicyFields() {
        PolicyRequest request = new PolicyRequest("P-1", "Example Insured", "Property",
            new BigDecimal("100.00"), LocalDate.parse("2026-01-01"), LocalDate.parse("2027-01-01"));
        assertEquals("P-1", request.policyNumber());
        assertEquals("Property", request.lob());
        assertTrue(request.expirationDate().isAfter(request.effectiveDate()));
    }
}
