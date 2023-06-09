package bg.softuni.pathfinder.repository;

import bg.softuni.pathfinder.model.entities.Route;
import bg.softuni.pathfinder.model.entities.enums.RouteCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {



    @Query(value = "select r from Route r join Comments c on r.id = c.routeId GROUP BY c.routeId ORDER BY COUNT(c.routeId) desc")
    List<Route> findByRouteCount(Pageable pageable);

    @Query("select r from Route r where r.isActive = false ")
    List<Route> findByIsActive();

    List<Route>findByCategorie(RouteCategory categorie);
}
