package batch_2.capstone.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "towers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Tower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long towerId;

    @Column(nullable = false)
    private String towerName;

    private String location;

    private Integer totalShares = 100; // Standard 100% ownership pool

    // --- ADDED FIELD FOR FRONTEND PERCENTAGE ---
    @Transient
    private Double currentOccupancy;

    @Enumerated(EnumType.STRING)
    private TowerStatus status = TowerStatus.ACTIVE;

    public enum TowerStatus {
        ACTIVE, UNDER_MAINTENANCE, INACTIVE
    }
}