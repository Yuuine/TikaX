package anthony.tikax.controller;

import anthony.tikax.entity.Result;
import anthony.tikax.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public Result<Object> register(String username, String password) {
        userService.register(username, password);
        return Result.success();

    }

    @PostMapping("/login")
    public Result<Object> login(String username, String password) {
        userService.login(username, password);
        return Result.success();
    }
}
