package bg.softuni.pathfinder.model.dto.binding;

import org.apache.tomcat.util.descriptor.web.MultipartDef;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RouteAddBinding {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String level;
    private String videoUrl;
    private MultipartFile image;
    private String categorie;


    public RouteAddBinding() {
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }


    public MultipartFile getImage() {
        return image;
    }




    public void setImage( MultipartFile image) {
        this.image = image;
    }
}
