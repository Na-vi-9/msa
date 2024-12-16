package com.sparta.msa.company.infrastructure.clients;

import com.sparta.msa.company.application.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "users")
public interface UserClient {

    @GetMapping("/users/{username}")
    UserDto getUserInfo(@RequestHeader("Authorization") String token,
                        @PathVariable("username") String username);
}
