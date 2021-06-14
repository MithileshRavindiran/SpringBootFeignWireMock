package com.tryout.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tryout.spring.feignclient.JSONPlaceHolderApiClient;
import com.tryout.spring.model.Post;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockMvcClientHttpRequestFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WebMvcTest(SampleController.class)
public class SampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JSONPlaceHolderApiClient jsonPlaceHolderApiClient;


    private List<Post> postList = new ArrayList<>();

    ObjectMapper objectMapper;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
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

    @Test
    public void whenGetPostsIsCalled_thenCallExecutesSuccessfully() throws Exception {
        when(jsonPlaceHolderApiClient.getPosts()).thenReturn(postList);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/posts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertAll("Validating Response",
                () -> assertNotNull(result),
                () -> assertEquals(objectMapper.writeValueAsString(postList),  result.getResponse().getContentAsString()));
    }

    @Test
    public void whenGetPostByIdIsCalled_thenCallExecutedSuccessfully() throws Exception {
        when(jsonPlaceHolderApiClient.getPostById(any())).thenReturn(postList.get(1));
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertAll("Validation Response",
                () -> assertNotNull(mvcResult),
                () -> assertEquals(objectMapper.writeValueAsString(postList.get(1)), mvcResult.getResponse().getContentAsString()));
    }
}
