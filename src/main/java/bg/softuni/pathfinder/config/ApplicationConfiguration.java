package bg.softuni.pathfinder.config;

import bg.softuni.pathfinder.model.dto.view.RouteDetailsView;
import bg.softuni.pathfinder.model.entities.Route;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.RouteMatcher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Configuration
public class ApplicationConfiguration {


    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        Converter<byte[] , String> toString = new Converter<byte[], String>() {
            @Override
            public String convert(MappingContext<byte[], String> context) {
              return Base64.getMimeEncoder().encodeToString(context.getSource());
            }

        };

        Converter<LocalDateTime,String > toStringDate = new AbstractConverter<LocalDateTime, String>() {
            @Override
            protected String convert(LocalDateTime source) {
                return source.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
            }
        };
        return modelMapper;

    }

}
