package com.sparta.msa.company.infrastructure.clients;

import com.sparta.msa.company.application.dto.UserDto;
import com.sparta.msa.company.presentation.exception.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "users", url = "http://localhost:19097")
public interface UserClient {

    @GetMapping("/users/info")
    CommonResponse<UserDto> getUserInfo(@RequestHeader("Authorization") String token,
                                       @RequestParam("username") String username);
}
