package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.dto.binding.ImageAddBinding;
import bg.softuni.pathfinder.model.dto.binding.RouteAddBinding;
import bg.softuni.pathfinder.model.dto.view.PicturesView;
import bg.softuni.pathfinder.model.dto.view.RouteDetailsView;
import bg.softuni.pathfinder.model.dto.view.RoutesView;

import bg.softuni.pathfinder.model.entities.Picture;
import bg.softuni.pathfinder.model.entities.Route;
import bg.softuni.pathfinder.model.entities.User;
import bg.softuni.pathfinder.model.entities.enums.RouteCategory;
import bg.softuni.pathfinder.repository.RouteRepository;
import org.modelmapper.ModelMapper;

import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class RoutesService {

    private final RouteRepository routeRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final PictureService pictureService;

    private final CommentService commentService;
    public RoutesService(RouteRepository routeRepository, ModelMapper modelMapper, UserService userService, PictureService pictureService, CommentService commentService) {
        this.routeRepository = routeRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.pictureService = pictureService;
        this.commentService = commentService;
    }

    public List<RoutesView> getAllRoutes(){
        List<Route> all = this.routeRepository.findAll();
       List<RoutesView> views = new ArrayList<>();

        for (Route route : all) {
            Set<Picture> pictures = route.getPictures();
            RoutesView currentView = modelMapper.map(route, RoutesView.class);

            for (Picture picture : pictures) {
                String image = Base64.getMimeEncoder().encodeToString(picture.getImage());
                currentView.setThumbnailUrl(image);
                currentView.setContentType(picture.getContentType());
            }
            views.add(currentView);
        }
        return views;
    }

    public void createRoute(RouteAddBinding routeAddBinding, String username) throws IOException {
        User userByUserName = userService.getUserByUserName(username);

        Route route = modelMapper.map(routeAddBinding, Route.class);
        route.setAuthorId(userByUserName.getId());
        route.setActive(false);
        routeRepository.save(route);
        savePicture(routeAddBinding, userByUserName, route);
    }

    private void savePicture(RouteAddBinding routeAddBinding, User userByUserName, Route route) throws IOException {
        ImageAddBinding img = new ImageAddBinding();
        img.setContentType(routeAddBinding.getImage().getContentType());
        img.setTitle(routeAddBinding.getImage().getOriginalFilename());
        img.setImage(routeAddBinding.getImage().getBytes());
        img.setAuthor(userByUserName);
        img.setRoute(route);

        pictureService.savePicture(img);
    }

    public RouteDetailsView getRouteById(Long id){

        Route route = this.routeRepository.findById(id).get();
        Set<PicturesView>pics = new HashSet<>();
        mapToPictureView(route, pics);

        return mapToView(route, pics);

    }

    private static void mapToPictureView(Route route, Set<PicturesView> pics) {

        route.getPictures().forEach(picture -> {
            PicturesView picturesView = new PicturesView();
            picturesView.setImage(Base64.getMimeEncoder().encodeToString(picture.getImage()));
            picturesView.setContentType(picture.getContentType());
            pics.add(picturesView);
        });
    }

    private static RouteDetailsView mapToView(Route route, Set<PicturesView> pics) {
        RouteDetailsView detailsView = new RouteDetailsView();
        detailsView.setPictures(pics);
        detailsView.setDescription(route.getDescription());
        detailsView.setAuthorId(route.getAuthorId());
        detailsView.setLevel(route.getLevel().name());
        detailsView.setVideoUrl(route.getVideoUrl());
        detailsView.setId(route.getId());
        detailsView.setName(route.getName());
        detailsView.setActive(route.isActive());
        return detailsView;
    }

    public RouteDetailsView getRouteWithMostComments() {

        try {
            return modelMapper.map(this.routeRepository.findByRouteCount(PageRequest.of(0,1))
                    .get(0), RouteDetailsView.class);
        }catch (Exception e){
            return null;
        }

    }

    public Route getById(Long id) {
        return routeRepository.findById(id).get();
    }

    public List<RoutesView> getRoutesByCategory() {
        List<Route> byCategorie = routeRepository.findByCategorie(RouteCategory.PEDESTRIAN);
        return getRoutesByCategory( byCategorie );
    }

    private List<RoutesView> getRoutesByCategory(List<Route> byCategorie) {
        List<RoutesView> listToReturn = new ArrayList<>();

        for (Route route : byCategorie) {
            RoutesView map = modelMapper.map(route, RoutesView.class);
            Picture picture = pictureService.getPicture(route.getId());
            map.setContentType(picture.getContentType());
            map.setThumbnailUrl(Base64.getMimeEncoder().encodeToString(picture.getImage()));
            listToReturn.add(map);
        }
        return listToReturn;
    }

    public List<RoutesView> getAllPedestrianRoutes() {
        return getRoutesByCategory( routeRepository.findByCategorie(RouteCategory.PEDESTRIAN));
    }
    public List<RoutesView> getAllBicycleRoutes() {
        return getRoutesByCategory( routeRepository.findByCategorie(RouteCategory.BICYCLE));
    }

    public List<RoutesView> getAllMotorcycleRoutes() {
        return getRoutesByCategory( routeRepository.findByCategorie(RouteCategory.MOTORCYCLE));
    }

    public List<RoutesView> getAllCarRoutes() {
        return getRoutesByCategory( routeRepository.findByCategorie(RouteCategory.CAR));
    }

    public void setRouteApproved(Long id, boolean isRouteActive) {
        Route route = routeRepository.findById(id).get();
        route.setActive(true);
        routeRepository.save(route);
    }


    //cron expression every day at midnight 00:00
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteNotApprovedRoutes(){
        this.routeRepository.findAll().stream().filter(route -> !route.isActive())
                .forEachOrdered(routeRepository::delete);
    }

    public void deleteRoute(Long id) {
        routeRepository.delete(routeRepository.findById(id).get());
    }

    public List<RouteDetailsView> getAllRoutesToBeApproved() {
        Boolean active = false;
        List<Route> allByActive = this.routeRepository.findByIsActive();
        List<RouteDetailsView> routes = new ArrayList<>();

        for (Route route : allByActive) {
            RouteDetailsView map = modelMapper.map(route, RouteDetailsView.class);
            routes.add(map);
        }
        return routes;
    }
}
