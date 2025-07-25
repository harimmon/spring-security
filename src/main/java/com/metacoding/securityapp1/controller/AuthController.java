package com.metacoding.securityapp1.controller;

import com.metacoding.securityapp1.controller.dto.UserRequest;
import com.metacoding.securityapp1.core.Resp;
import com.metacoding.securityapp1.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;

    /**
     * username=ssar&password=1234&email=ssar@nate.com <- x-www-form-urlencoded
     * {"username":"ssar", "password":1234, "email":"ssar@nate.com"} <- json
     */
    @PostMapping("/join")
    public Resp<?> join(@RequestBody UserRequest.Join reqDTO) {
        userService.회원가입(reqDTO);
        return new Resp<>();
    }

    @PostMapping("/login")
    public Resp<?> login(@RequestBody UserRequest.Login reqDTO) {
        String accessToken = userService.로그인(reqDTO);
        return new Resp<>(accessToken);
    }
}