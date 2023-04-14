package bg.softuni.pathfinder.repository;

import bg.softuni.pathfinder.model.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
