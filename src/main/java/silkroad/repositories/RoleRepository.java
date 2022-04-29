package silkroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import silkroad.entities.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
}