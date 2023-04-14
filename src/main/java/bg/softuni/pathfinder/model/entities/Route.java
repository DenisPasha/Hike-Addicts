package bg.softuni.pathfinder.model.entities;

import bg.softuni.pathfinder.model.entities.enums.Level;
import bg.softuni.pathfinder.model.entities.enums.RouteCategory;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    private String gpxCoordinates;


    @Enumerated(EnumType.STRING)
    private Level level;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private Long authorId;

    private String videoUrl;

    @OneToMany(mappedBy = "route", fetch = FetchType.EAGER ,cascade = CascadeType.ALL)
    private Set<Picture> pictures;

    @Enumerated(EnumType.STRING)
    private RouteCategory categorie;

    private boolean isActive ;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGpxCoordinates() {
        return gpxCoordinates;
    }

    public void setGpxCoordinates(String gpxCoordinates) {
        this.gpxCoordinates = gpxCoordinates;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }

    public RouteCategory getCategorie() {
        return categorie;
    }

    public void setCategorie(RouteCategory categorie) {
        this.categorie = categorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
