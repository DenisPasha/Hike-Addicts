package bg.softuni.pathfinder.config;

import bg.softuni.pathfinder.repository.UserRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfiguration {

    public MetricsConfiguration (MeterRegistry meterRegistry , UserRepository userRepository){

        Gauge.builder("users.count", userRepository::count)
                .register(meterRegistry);

    }
}
