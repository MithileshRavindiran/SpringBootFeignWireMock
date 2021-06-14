package com.tryout.spring.feignclient;

import com.tryout.spring.config.CustomFeignClientConfiguration;
import com.tryout.spring.config.JSONPlaceHolderFallback;
import com.tryout.spring.model.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient(value = "${app.feign.client.name}", url = "${app.feign.client.url}", configuration = CustomFeignClientConfiguration.class, fallback = JSONPlaceHolderFallback.class)
public interface JSONPlaceHolderApiClient {
    
    @RequestMapping(method=RequestMethod.GET, value="/posts")
    List<Post> getPosts();

    @RequestMapping(method = RequestMethod.GET, value = "/posts/{postId}", produces = "application/json")
    Post getPostById(@PathVariable("postId") Long postId);
    
}
