package batch_2.capstone.repository;



import batch_2.capstone.model.Tower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TowerRepository extends JpaRepository<Tower, Long> {
    // Basic CRUD operations are handled by JpaRepository
}