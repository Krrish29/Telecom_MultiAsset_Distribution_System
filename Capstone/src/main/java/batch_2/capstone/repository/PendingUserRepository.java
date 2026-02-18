package batch_2.capstone.repository;

import batch_2.capstone.model.PendingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PendingUserRepository extends JpaRepository<PendingUser, Long> {
    Optional<PendingUser> findBySessionId(String sessionId);
}