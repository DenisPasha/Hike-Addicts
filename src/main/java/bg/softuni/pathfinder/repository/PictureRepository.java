package bg.softuni.pathfinder.repository;

import bg.softuni.pathfinder.model.entities.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<Picture , Long> {
    Optional<Picture> findByRouteId(long route_id);
}
