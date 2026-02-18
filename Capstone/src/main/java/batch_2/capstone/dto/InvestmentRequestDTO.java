package batch_2.capstone.dto;


import lombok.Data;

/**
 * Used when a user wants to buy shares.
 * Frontend sends: { "userId": 1, "towerId": 5, "sharePercentage": 10.0 }
 */
@Data
public class InvestmentRequestDTO {
    private Long userId;
    private Long towerId;
    private Double sharePercentage;
}