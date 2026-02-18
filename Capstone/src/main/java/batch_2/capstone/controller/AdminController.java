package batch_2.capstone.controller;

import batch_2.capstone.model.Tower;
import batch_2.capstone.model.UserShare;
import batch_2.capstone.repository.TowerRepository;
import batch_2.capstone.repository.UserRepository;
import batch_2.capstone.repository.UserShareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private TowerRepository towerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserShareRepository userShareRepository;

    @PostMapping("/towers")
    public Tower createTower(@RequestBody Tower tower) {
        // Ensuring default status if not provided
        if (tower.getStatus() == null) {
            tower.setStatus(Tower.TowerStatus.ACTIVE);
        }
        return towerRepository.save(tower);
    }

    // New Endpoint to update only the status
    @PatchMapping("/tower/{towerId}/status")
    public ResponseEntity<?> updateTowerStatus(@PathVariable Long towerId, @RequestParam Tower.TowerStatus status) {
        return towerRepository.findById(towerId).map(tower -> {
            tower.setStatus(status);
            towerRepository.save(tower);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Status updated successfully");
            return ResponseEntity.ok().body(response);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user-investments")
    public List<Map<String, Object>> getSummary() {
        return userRepository.findAll().stream().map(user -> {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("fullName", user.getFullName());
            userMap.put("email", user.getEmail());

            List<UserShare> shares = userShareRepository.findByUser_UserId(user.getUserId());

            List<Map<String, Object>> shareList = shares.stream().map(s -> {
                Map<String, Object> sMap = new HashMap<>();
                sMap.put("shareId", s.getShareId());
                sMap.put("towerId", s.getTower().getTowerId()); // Needed for status toggle
                sMap.put("towerName", s.getTower().getTowerName());
                sMap.put("location", s.getTower().getLocation()); // Added
                sMap.put("percentage", s.getSharePercentage());
                sMap.put("status", s.getTower().getStatus()); // Added
                return sMap;
            }).collect(Collectors.toList());

            userMap.put("shares", shareList);
            return userMap;
        }).collect(Collectors.toList());
    }

    @DeleteMapping("/investment/{shareId}")
    public ResponseEntity<?> deleteInvestment(@PathVariable Long shareId) {
        if (userShareRepository.existsById(shareId)) {
            userShareRepository.deleteById(shareId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Deleted successfully");
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Investment record not found");
    }
}