package dev.portfolio.insurance;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "policies", indexes = @Index(name = "idx_policy_number", columnList = "policyNumber", unique = true))
public class Policy {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true) private String policyNumber;
    @Column(nullable = false) private String insuredName;
    @Column(nullable = false) private String lob;
    @Column(nullable = false, precision = 14, scale = 2) private BigDecimal premium;
    @Column(nullable = false) private LocalDate effectiveDate;
    @Column(nullable = false) private LocalDate expirationDate;

    protected Policy() {}
    public Policy(String policyNumber, String insuredName, String lob, BigDecimal premium,
                  LocalDate effectiveDate, LocalDate expirationDate) {
        this.policyNumber = policyNumber; this.insuredName = insuredName; this.lob = lob;
        this.premium = premium; this.effectiveDate = effectiveDate; this.expirationDate = expirationDate;
    }
    public Long getId() { return id; }
    public String getPolicyNumber() { return policyNumber; }
    public String getInsuredName() { return insuredName; }
    public String getLob() { return lob; }
    public BigDecimal getPremium() { return premium; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public LocalDate getExpirationDate() { return expirationDate; }
}
