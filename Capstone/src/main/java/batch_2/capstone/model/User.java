package batch_2.capstone.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // NEW: Encrypted password

    private String role = "USER"; // NEW: Default role

    private BigDecimal walletBalance = BigDecimal.ZERO;
    // Add these to your User class manually to bypass Lombok
    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getWalletBalance() {
        return walletBalance;
    }

    public String getFullName() {
        return fullName;
    }
}