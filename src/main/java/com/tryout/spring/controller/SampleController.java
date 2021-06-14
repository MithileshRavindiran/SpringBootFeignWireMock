package com.tryout.spring.controller;

import com.tryout.spring.feignclient.JSONPlaceHolderApiClient;
import com.tryout.spring.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SampleController {

    private final JSONPlaceHolderApiClient jsonPlaceHolderApiClient;

    @GetMapping(value = "/posts")
    public ResponseEntity<List<Post>> getPosts() {
        return ResponseEntity.ok(jsonPlaceHolderApiClient.getPosts());
    }

    @GetMapping(value = "/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(jsonPlaceHolderApiClient.getPostById(Long.valueOf(id)));
    }
}
