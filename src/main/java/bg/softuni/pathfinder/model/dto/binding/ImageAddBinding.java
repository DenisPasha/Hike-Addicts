package bg.softuni.pathfinder.model.dto.binding;

import bg.softuni.pathfinder.model.entities.Route;
import bg.softuni.pathfinder.model.entities.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

public class ImageAddBinding {

    private String title;


    private String contentType;


    @NotNull
    private byte[] image;

    private User author;


    private Route route;

    public ImageAddBinding() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
