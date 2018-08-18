package de.elasticrew.analyser.controllers;

import de.elasticrew.analyser.models.AppVersion;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static de.elasticrew.analyser.configs.AppConstants.EndPoints.VERSION;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class AppVersionControllerTest {

    private AppVersion testAppVersion;
    private AppVersionController controller;
    private MockMvc mvc;

    @BeforeClass
    public void setUpBeforeClass() {
        testAppVersion = new AppVersion("test-app", "test-version");
        controller = new AppVersionController(testAppVersion);
        mvc = standaloneSetup(controller).build();
    }

    @Test
    public void testGetAppVersion() throws Exception {
        mvc.perform(get(VERSION))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is(testAppVersion.getName())))
                .andExpect(jsonPath("$.version", is(testAppVersion.getVersion())));
    }
}
