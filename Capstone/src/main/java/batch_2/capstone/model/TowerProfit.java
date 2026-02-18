package batch_2.capstone.model;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tower_profits")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TowerProfit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profitId;

    @ManyToOne
    @JoinColumn(name = "tower_id", nullable = false)
    private Tower tower;

    @Column(nullable = false)
    private BigDecimal amount; // The total profit to be split

    private LocalDate calculationDate;

    private Boolean distributedStatus = false; // Tracks if money was sent to users
}