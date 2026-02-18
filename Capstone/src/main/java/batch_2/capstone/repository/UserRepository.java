package batch_2.capstone.repository;
import batch_2.capstone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Find user by email for login/authentication purposes
    Optional<User> findByEmail(String email);
}