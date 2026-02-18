package batch_2.capstone.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "batch_2.capstone.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    // This class enables JPA and Transaction management across your service layer.
    // It ensures that profit distributions are atomic (all succeed or all fail).
}