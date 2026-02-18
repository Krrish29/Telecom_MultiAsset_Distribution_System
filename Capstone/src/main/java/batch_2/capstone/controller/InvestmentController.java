package batch_2.capstone.controller;

import batch_2.capstone.dto.PayoutResponseDTO;
import batch_2.capstone.model.*;
import batch_2.capstone.repository.*;
import batch_2.capstone.service.ProfitDistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/investment")
@CrossOrigin(origins = "*")
public class InvestmentController {

    @Autowired
    private TowerRepository towerRepository;
    @Autowired
    private UserShareRepository userShareRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfitDistributionService profitService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- 1. REGISTRATION ---
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> payload) {
        if (userRepository.findByEmail(payload.get("email")).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is already registered!"));
        }

        User newUser = new User();
        newUser.setFullName(payload.get("fullName"));
        newUser.setEmail(payload.get("email"));
        newUser.setPassword(passwordEncoder.encode(payload.get("password")));

        BigDecimal initialBalance = payload.get("walletBalance") != null ?
                new BigDecimal(payload.get("walletBalance")) : BigDecimal.ZERO;
        newUser.setWalletBalance(initialBalance);
        newUser.setRole("ROLE_USER");

        userRepository.save(newUser);
        return ResponseEntity.ok(Map.of("message", "Registration successful!"));
    }

    // --- 2. SECURE BUY SHARES ---
    @PostMapping("/buy-shares")
    @Transactional
    public ResponseEntity<?> investInTower(@RequestBody Map<String, Object> payload,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Extract IDs safely from the nested JSON structure
            Map<String, Object> userMap = (Map<String, Object>) payload.get("user");
            Map<String, Object> towerMap = (Map<String, Object>) payload.get("tower");

            Long userId = Long.valueOf(userMap.get("userId").toString());
            Long towerId = Long.valueOf(towerMap.get("towerId").toString());
            Double sharePercentage = Double.valueOf(payload.get("sharePercentage").toString());

            // Security Check: Ensure the logged-in user matches the ID in the request
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Logged in user session not found"));

            if (!user.getUserId().equals(userId)) {
                return ResponseEntity.status(403).body("Unauthorized: You cannot purchase shares for another user.");
            }

            Tower tower = towerRepository.findById(towerId)
                    .orElseThrow(() -> new RuntimeException("Tower not found"));

            // Check availability
            List<UserShare> existingShares = userShareRepository.findByTower_TowerId(towerId);
            double currentTotal = existingShares.stream().mapToDouble(UserShare::getSharePercentage).sum();
            if (sharePercentage > (100.0 - currentTotal)) {
                return ResponseEntity.badRequest().body("Insufficient shares available in this tower.");
            }

            // Calculate cost ($100 per 1%)
            BigDecimal totalCost = new BigDecimal("100.00").multiply(BigDecimal.valueOf(sharePercentage));
            if (user.getWalletBalance().compareTo(totalCost) < 0) {
                return ResponseEntity.badRequest().body("Insufficient Wallet Balance!");
            }

            // Execute Transaction
            user.setWalletBalance(user.getWalletBalance().subtract(totalCost));
            userRepository.save(user);

            UserShare newShare = new UserShare();
            newShare.setUser(user);
            newShare.setTower(tower);
            newShare.setSharePercentage(sharePercentage);
            userShareRepository.save(newShare);

            return ResponseEntity.ok(Map.of("message", "Investment Successful!", "newBalance", user.getWalletBalance()));

        } catch (Exception e) {
            e.printStackTrace(); // Logs the specific error to your terminal
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }

    // --- 3. OTHER ENDPOINTS ---
    @GetMapping("/my-shares/{userId}")
    public ResponseEntity<List<UserShare>> getUserInvestments(@PathVariable Long userId) {
        return ResponseEntity.ok(userShareRepository.findByUser_UserId(userId));
    }

    @GetMapping("/towers")
    public List<Tower> getAvailableTowers() {
        List<Tower> towers = towerRepository.findAll();
        towers.forEach(t -> {
            Double occupancy = userShareRepository.findByTower_TowerId(t.getTowerId()).stream()
                    .mapToDouble(UserShare::getSharePercentage).sum();
            t.setCurrentOccupancy(occupancy);
        });
        return towers;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}