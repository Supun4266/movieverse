package com.movieverse.backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

/**
 * Tests the API end to end with an in-memory database and Kafka turned off.
 * Run with: ./mvnw test
 */
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:movieverse-test;DB_CLOSE_DELAY=-1",
        "movieverse.kafka.enabled=false"
})
@AutoConfigureMockMvc
class MovieVerseApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void anyoneCanBrowseMovies() throws Exception {
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(25));
    }

    @Test
    void canFilterMoviesByGenre() throws Exception {
        mockMvc.perform(get("/api/movies").param("genre", "Animation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].genre").value("Animation"));
    }

    @Test
    void unknownMovieReturns404() throws Exception {
        mockMvc.perform(get("/api/movies/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie 99999 not found"));
    }

    @Test
    void myListNeedsLogin() throws Exception {
        mockMvc.perform(get("/api/my-list"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shortPasswordIsRejected() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"shorty\",\"password\":\"123\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void userCanRegisterSaveMovieAndTrackProgress() throws Exception {
        String token = registerAndGetToken("tester", "secret123");

        mockMvc.perform(post("/api/my-list/1").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/my-list").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));

        mockMvc.perform(post("/api/watch/2/start").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/watch/2")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"progressPercent\":40}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.progressPercent").value(40));

        mockMvc.perform(get("/api/watch/continue").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movie.id").value(2))
                .andExpect(jsonPath("$[0].progressPercent").value(40));
    }

    @Test
    void wrongPasswordIsRejected() throws Exception {
        registerAndGetToken("alice", "password1");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"alice\",\"password\":\"wrong-password\"}"))
                .andExpect(status().isUnauthorized());
    }

    private String registerAndGetToken(String username, String password) throws Exception {
        String body = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.token");
    }
}
