package bg.softuni.pathfinder.repository;

import bg.softuni.pathfinder.model.entities.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments , Long> {
    List<Comments> findAllByRouteId(long id);
}
