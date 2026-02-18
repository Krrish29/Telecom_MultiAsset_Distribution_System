package batch_2.capstone.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PendingUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String password; // NEW: To store the hashed password during payment redirect
    private BigDecimal initialDeposit;
    private String sessionId;
    private String status;
}