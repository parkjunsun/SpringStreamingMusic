package js.StreamingMusic.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
@PropertySource("application.properties")
public class ConfigUtil {

    private final Environment environment;

    public String getProperty(String key) {
        return environment.getProperty(key);
    }

}
