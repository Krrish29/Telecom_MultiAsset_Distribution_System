package batch_2.capstone.repository;

import batch_2.capstone.model.TowerProfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TowerProfitRepository extends JpaRepository<TowerProfit, Long> {

    // Find all profits for a specific tower
    List<TowerProfit> findByTower_TowerId(Long towerId);

    // Find profits that haven't been distributed yet
    List<TowerProfit> findByDistributedStatusFalse();
}
