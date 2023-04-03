package bg.softuni.pathfinder.repository;

import bg.softuni.pathfinder.model.entities.Route;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {



    @Query(value = "select r from Route r join Comments c on r.id = c.routeId GROUP BY c.routeId ORDER BY COUNT(c.routeId) desc")
    List<Route> findByRouteCount(Pageable pageable);


}
