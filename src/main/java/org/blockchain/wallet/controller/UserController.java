package org.blockchain.wallet.controller;

import lombok.RequiredArgsConstructor;
import org.blockchain.wallet.base.BaseResponse;
import org.blockchain.wallet.base.ResultResponse;
import org.blockchain.wallet.entity.User;
import org.blockchain.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/login")
    public BaseResponse<User> login(@RequestBody User user) {

        return new ResultResponse<>(userService.login(user));
    }

    @PostMapping(value = "/register")
    public BaseResponse<User> register(@RequestBody User user) {

        return new ResultResponse<>(userService.createUser(user));
    }

    @GetMapping(value = "/info")
    public BaseResponse<User> getUserInfo(@RequestParam Integer userId) {

        return new ResultResponse<>(userService.findUserById(userId));
    }

    @PutMapping(value = "/update")
    public BaseResponse<User> updateUserInfo(@RequestBody User user) {

        return new ResultResponse<>(userService.updateUser(user));
    }
}
