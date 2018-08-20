package de.elasticrew.analyser.configs;

import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static de.elasticrew.analyser.configs.AppConstants.AppProperties.APPLICATION_NAME;
import static de.elasticrew.analyser.configs.AppConstants.AppProperties.APPLICATION_VERSION;

@Configuration
public class AppInfoConfig {

    private static final String APP_INFO_NAME = "application";
    private static final String APP_INFO_VERSION = "version";

    @Bean
    public InfoContributor appInfo(final Environment env) {
        return (appInfo) -> appInfo
                .withDetail(APP_INFO_NAME, env.getRequiredProperty(APPLICATION_NAME))
                .withDetail(APP_INFO_VERSION, env.getRequiredProperty(APPLICATION_VERSION));
    }
}
