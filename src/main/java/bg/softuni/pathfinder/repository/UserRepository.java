package bg.softuni.pathfinder.repository;

import bg.softuni.pathfinder.model.entities.User;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findById(long id);

    User save(User user);

    @Query("select u from User u where u.isActive = true")
    List<User> findAllApprovedUsers();

    @Query("select u from User u where u.isActive = false")
    List<User> findAllNotApprovedUsers();
}
