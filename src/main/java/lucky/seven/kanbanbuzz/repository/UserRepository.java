package lucky.seven.kanbanbuzz.repository;

import java.util.Optional;
import lucky.seven.kanbanbuzz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long > {

    Optional<User> findByEmail(String userEmail);


}
