package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.dto.binding.RouteAddBinding;
import bg.softuni.pathfinder.model.dto.view.RouteDetailsView;
import bg.softuni.pathfinder.model.dto.view.RoutesView;
import bg.softuni.pathfinder.model.entities.Comments;
import bg.softuni.pathfinder.model.entities.Picture;
import bg.softuni.pathfinder.model.entities.Route;
import bg.softuni.pathfinder.model.entities.User;
import bg.softuni.pathfinder.model.entities.enums.Level;
import bg.softuni.pathfinder.model.entities.enums.RouteCategory;
import bg.softuni.pathfinder.repository.RouteRepository;
import jdk.dynalink.linker.LinkerServices;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoutesServiceTest {



    private  RouteRepository routeRepository;
    private  ModelMapper modelMapper;
    private  UserService userService;
    private  PictureService pictureService;
    private  CommentService commentService;

    private RoutesService routesService;

    @BeforeEach
    public void setUp() throws Exception {
        routeRepository = Mockito.mock(RouteRepository.class);
        modelMapper = new ModelMapper();
        userService = Mockito.mock(UserService.class);
        pictureService = Mockito.mock(PictureService.class);
        commentService = Mockito.mock(CommentService.class);

        routesService = new RoutesService(routeRepository , modelMapper ,userService ,pictureService ,commentService);
    }



    @Test
    public void getAllRoutesTest(){
        Route route1 = Mockito.mock(Route.class);
        Route route2 = Mockito.mock(Route.class);
        Route route3 = Mockito.mock(Route.class);

        List<Route> routes = new ArrayList<>();
        routes.add(route1);
        routes.add(route2);
        routes.add(route3);

        when(routeRepository.findAll()).thenReturn(routes);

        List<RoutesView> allRoutes = routesService.getAllRoutes();
        assertEquals(routes.size() , allRoutes.size());
    }

    @Test
    public void createRouteTest() throws IOException {
        RouteAddBinding routeAddBinding = new RouteAddBinding();
        User author = new User();
        author.setId(1);
        author.setUsername("author");

        routeAddBinding.setName("route name");
        routeAddBinding.setDescription("description");
        routeAddBinding.setImage(Mockito.mock(MultipartFile.class));
        routeAddBinding.setLevel(Level.BEGINNER.name());
        routeAddBinding.setVideoUrl("vudsoij");
        routeAddBinding.setCategorie(RouteCategory.CAR.name());

        when(userService.getUserByUserName("author")).thenReturn(author);
        routesService.createRoute(routeAddBinding ,author.getUsername());

        Mockito.verify(routeRepository).save(any(Route.class));
    }

    @Test
    public void getRouteByIdTest(){

        Route route = new Route();
        route.setPictures(Set.of());
        route.setAuthorId(1L);
        route.setLevel(Level.BEGINNER);
        route.setActive(true);
        route.setName("name");
        route.setVideoUrl("daa");
        route.setCategorie(RouteCategory.CAR);

        when(routeRepository.findById(any())).thenReturn(Optional.of(route));

        RouteDetailsView routeById = routesService.getRouteById(1L);

        assertEquals(route.getId() , routeById.getId());
    }

    @Test
    public void getByIdTest(){
        Route route = new Route();
        route.setPictures(Set.of());
        route.setAuthorId(1L);
        route.setLevel(Level.BEGINNER);
        route.setActive(true);
        route.setName("name");
        route.setVideoUrl("daa");
        route.setCategorie(RouteCategory.CAR);

        when(routeRepository.findById(1L)).thenReturn(Optional.of(route));
        Route byId = routesService.getById(1L);

        assertEquals(route.getId() , byId.getId());
    }

    @Test
    public void getAllPedestrianRoutesTest(){
        Route route = new Route();

        Picture mockPic = getPicture(route);

        route.setPictures(Set.of(mockPic));
        route.setAuthorId(1L);
        route.setLevel(Level.BEGINNER);
        route.setActive(true);
        route.setName("name");
        route.setVideoUrl("daa");
        route.setCategorie(RouteCategory.PEDESTRIAN);

        Route route1 = new Route();
        route.setPictures(Set.of(mockPic));
        route.setAuthorId(2L);
        route.setLevel(Level.ADVANCED);
        route.setActive(true);
        route.setName("name pedestrian");
        route.setVideoUrl("mdmwklq");
        route.setCategorie(RouteCategory.PEDESTRIAN);

        List<Route>pedestrianRoutes = new ArrayList<>();
        pedestrianRoutes.add(route);
        pedestrianRoutes.add(route1);

        when(routeRepository.findByCategorie(RouteCategory.PEDESTRIAN)).thenReturn(pedestrianRoutes);
        when(pictureService.getPicture(any())).thenReturn(mockPic);

        List<RoutesView> allPedestrianRoutes = routesService.getAllPedestrianRoutes();

        assertEquals(pedestrianRoutes.size() , allPedestrianRoutes.size());

    }

    private static Picture getPicture(Route route) {
        Picture mockPic = new Picture();
        mockPic.setContentType("jpeg");
        mockPic.setRoute(route);
        mockPic.setTitle("title");
        mockPic.setAuthor(new User());
        mockPic.setId(1);
        mockPic.setImage(new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        return mockPic;
    }

}