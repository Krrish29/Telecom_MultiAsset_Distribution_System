package batch_2.capstone.repository;

import batch_2.capstone.model.UserShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserShareRepository extends JpaRepository<UserShare, Long> {

    // Finds all investors for a specific tower (used for profit distribution)
    List<UserShare> findByTower_TowerId(Long towerId);

    // Finds all towers a specific user has invested in
    List<UserShare> findByUser_UserId(Long userId);
}
