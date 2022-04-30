package silkroad.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import silkroad.dtos.user.request.UserSecurityDetails;
import silkroad.dtos.user.response.UserBasicDetails;
import silkroad.entities.User;

import java.util.Optional;

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

//    @Query("SELECT new silkroad.dtos.user.response.UserBasicDetails(u.username, u.role.name, u.approved, u.joinDate) FROM User u")
//    Page<UserBasicDetails> findAllUsersBasicDetails(Specification<UserBasicDetails> userSpecification, Pageable pageable);

}