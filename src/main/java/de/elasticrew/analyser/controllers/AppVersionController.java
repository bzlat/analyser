package de.elasticrew.analyser.controllers;

import de.elasticrew.analyser.models.AppVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static de.elasticrew.analyser.configs.AppConstants.EndPoints.VERSION;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(VERSION)
public class AppVersionController {


    private final AppVersion appVersion;

    @Autowired
    public AppVersionController(final AppVersion appVersion) {
        this.appVersion = appVersion;
    }

    @GetMapping
    public ResponseEntity<AppVersion> version() {
        return ok(appVersion);
    }
}
