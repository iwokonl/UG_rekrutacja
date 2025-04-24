package pl.ug.config;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {

    private final String timeZone;

    public TimeZoneConfig(@Value("${app.timezone}") String timeZone) {
        this.timeZone = timeZone;
    }

    @Bean
    public String getZoneId() {
        return timeZone;
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

}
