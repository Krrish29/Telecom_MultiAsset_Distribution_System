package batch_2.capstone.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Clean object to send payout data to the frontend.
 * We use BigDecimal to ensure currency precision.
 */
public record PayoutResponseDTO(
        String investorName,
        String email,
        String towerName,
        BigDecimal payoutAmount,
        LocalDate calculationDate
) {}