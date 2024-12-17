package com.sparta.msa.ai.domain.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "users", url = "http://localhost:19097")
public interface UserFeignClient {

    @GetMapping("/users/slack-id")
    String getSlackIdByUsername(@RequestParam("username") String username,
                                @RequestHeader("Authorization") String token);
}
