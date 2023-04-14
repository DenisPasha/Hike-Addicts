package bg.softuni.pathfinder.model.dto.view;

import bg.softuni.pathfinder.model.entities.Picture;

import java.util.Set;

public class RouteDetailsView {
    private Long id;
    private String description;
    private String level;
    private String name;
    private String videoUrl;
    private Long authorId;
    private Long pictureRouteId;

    private String thumbnailUrl;

    private String contentType ;

    private Set<PicturesView> pictures;

    private Boolean isActive;

    public RouteDetailsView() {
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getPictureRouteId() {
        return pictureRouteId;
    }

    public void setPictureRouteId(Long pictureRouteId) {
        this.pictureRouteId = pictureRouteId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Set<PicturesView> getPictures() {
        return pictures;
    }

    public void setPictures(Set<PicturesView> pictures) {
        this.pictures = pictures;
    }
}
