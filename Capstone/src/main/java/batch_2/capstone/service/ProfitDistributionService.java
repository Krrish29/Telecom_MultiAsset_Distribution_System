package batch_2.capstone.service;

import batch_2.capstone.dto.PayoutResponseDTO;
import batch_2.capstone.model.TowerProfit;
import batch_2.capstone.model.UserShare;
import batch_2.capstone.repository.TowerProfitRepository;
import batch_2.capstone.repository.UserShareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfitDistributionService {

    @Autowired
    private UserShareRepository shareRepo;

    @Autowired
    private TowerProfitRepository profitRepo;

    /**
     * Calculates the share of profit for each user associated with a specific tower.
     */
    @Transactional(readOnly = true)
    public List<PayoutResponseDTO> calculateDistributions(Long towerId, Long profitId) {
        // 1. Fetch the profit record
        TowerProfit profitRecord = profitRepo.findById(profitId)
                .orElseThrow(() -> new RuntimeException("Profit record not found"));

        // 2. Fetch all shareholders for this tower
        List<UserShare> shareholders = shareRepo.findByTower_TowerId(towerId);

        // 3. Map to DTO while performing the financial calculation
        return shareholders.stream().map(share -> {
            // Precision math: (Profit * Percentage) / 100
            BigDecimal userProfit = profitRecord.getAmount()
                    .multiply(BigDecimal.valueOf(share.getSharePercentage()))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            // Ensure this matches the fields in your PayoutResponseDTO
            return new PayoutResponseDTO(
                    share.getUser().getFullName(),
                    share.getUser().getEmail(),
                    share.getTower().getTowerName(),
                    userProfit,
                    profitRecord.getCalculationDate()
            );
        }).collect(Collectors.toList());
    }
}