package silkroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import silkroad.dtos.user.UserSecurityDTO;
import silkroad.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @Transactional
    @Query("SELECT new silkroad.dtos.user.UserSecurityDTO(u.username, u.password, u.approved, u.role.name) FROM User u WHERE u.username = ?1")
    Optional<UserSecurityDTO> findByUsername(String username);

    @Transactional
    Boolean existsByUsername(String username);

    @Transactional
    Boolean existsByEmail(String email);


}