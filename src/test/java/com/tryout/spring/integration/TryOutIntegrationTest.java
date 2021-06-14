package com.tryout.spring.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.tryout.spring.FeignWiremockApplication;
import com.tryout.spring.model.Post;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WireMockInitializer.class}, classes = {FeignWiremockApplication.class})
@AutoConfigureMockMvc
public class TryOutIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WireMockServer wireMockServer;

    ObjectMapper objectMapper;

    private List<Post> postList = new ArrayList<>();

    @BeforeEach
    public void setup() {
        postList.add(getPost("body", "title", Long.valueOf(1), "1"));
        postList.add(getPost("body1", "title1", Long.valueOf(2), "2"));
        objectMapper = new ObjectMapper();
    }

    private Post getPost(String  body, String title,  Long id, String userId) {
        Post post = new Post();
        post.setBody(body);
        post.setId(id);
        post.setTitle(title);
        post.setUserId(userId);
        return post;
    }

    @AfterEach
    public  void afterEach() {
        this.wireMockServer.resetAll();
    }

    @Test
    public void getPosts() throws Exception {
        wireMockServer
                .stubFor(get(WireMock.urlEqualTo("/posts"))
                                .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                 .withHeader("content-type", "application/json")
                                 .withBody(objectMapper.writeValueAsString(postList))));
        mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/posts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }
}
