package de.elasticrew.analyser.configs;

import de.elasticrew.analyser.models.AppVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import static de.elasticrew.analyser.configs.AppConstants.AppProperties.APPLICATION_NAME;
import static de.elasticrew.analyser.configs.AppConstants.AppProperties.APPLICATION_VERSION;

@Configuration
@PropertySource("classpath:/version.properties")
public class AppVersionConfig {

    @Bean
    public AppVersion appVersion(final Environment env) {
        return new AppVersion(
                env.getRequiredProperty(APPLICATION_NAME),
                env.getRequiredProperty(APPLICATION_VERSION));
    }
}
