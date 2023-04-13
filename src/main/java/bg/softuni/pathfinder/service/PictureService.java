package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.dto.binding.ImageAddBinding;
import bg.softuni.pathfinder.model.entities.Picture;
import bg.softuni.pathfinder.repository.PictureRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PictureService {

    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;

    public PictureService(PictureRepository pictureRepository, ModelMapper modelMapper) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
    }

    public void savePicture(ImageAddBinding img){
        pictureRepository.save(modelMapper.map(img , Picture.class));
    }

    public Picture getPicture(Long id) {
       return pictureRepository.findByRouteId(id).orElse(null);
    }
}

