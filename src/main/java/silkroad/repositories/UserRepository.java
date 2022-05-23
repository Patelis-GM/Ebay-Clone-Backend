package silkroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import silkroad.dtos.user.request.UserSecurityDetails;
import silkroad.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    @Query("SELECT new silkroad.dtos.user.request.UserSecurityDetails(u.username, u.password, u.approved, u.role.name) FROM User u WHERE u.username = ?1")
    Optional<UserSecurityDetails> findByUsername(String username);

    @Query("SELECT u FROM User u JOIN FETCH u.address WHERE u.username = ?1")
    Optional<User> findWithAddressByUsername(String username);

    @Transactional
    Boolean existsByUsername(String username);

    @Transactional
    Boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.approved = ?2 WHERE u.username = ?1")
    Integer updateApprovalStatusByUsername(String username, Boolean approvalStatus);


    @Query("SELECT u.username FROM User u WHERE u.approved = TRUE ORDER BY u.username ASC")
    List<String> findSortedUsernames();



}