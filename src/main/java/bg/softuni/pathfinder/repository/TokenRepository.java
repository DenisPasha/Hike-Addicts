package bg.softuni.pathfinder.repository;

import bg.softuni.pathfinder.model.entities.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<EmailConfirmationToken , Long> {

    Optional<EmailConfirmationToken> findEmailConfirmationTokenByToken(String token);
}
