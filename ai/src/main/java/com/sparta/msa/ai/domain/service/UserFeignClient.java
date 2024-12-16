package com.sparta.msa.ai.domain.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user", url = "http://localhost:19097")
public interface UserFeignClient {

    @GetMapping("/users/slack-id")
    String getSlackIdByUsername(@RequestParam String username);
}
