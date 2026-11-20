package dev.portfolio.insurance;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PolicyRequest(
    @NotBlank String policyNumber,
    @NotBlank String insuredName,
    @NotBlank String lob,
    @NotNull @DecimalMin("0.01") BigDecimal premium,
    @NotNull LocalDate effectiveDate,
    @NotNull LocalDate expirationDate
) {}
