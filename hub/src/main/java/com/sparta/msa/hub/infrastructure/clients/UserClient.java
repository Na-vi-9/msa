package com.sparta.msa.hub.infrastructure.clients;

import com.sparta.msa.hub.application.dto.UserDto;
import com.sparta.msa.hub.presentation.exception.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "users", url = "http://localhost:19097")
public interface UserClient {

    @GetMapping("/users/{username}")
    CommonResponse<UserDto> getUserInfo(@RequestHeader("Authorization") String token,
                                        @PathVariable("username") String username);
}
