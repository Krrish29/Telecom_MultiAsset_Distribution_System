package batch_2.capstone.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_shares")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Added for easier object creation in the controller
public class UserShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shareId;

    @ManyToOne(fetch = FetchType.EAGER) // Ensures tower/user data is loaded when fetched
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tower_id", nullable = false)
    private Tower tower;

    @Column(nullable = false)
    private Double sharePercentage;

    @Column(nullable = false, updatable = false)
    private LocalDateTime purchaseDate;

    // This ensures the date is set automatically before the record is saved to DB
    @PrePersist
    protected void onCreate() {
        this.purchaseDate = LocalDateTime.now();
    }
}