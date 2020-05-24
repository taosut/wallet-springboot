package org.blockchain.wallet.controller;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.blockchain.wallet.base.BaseResponse;
import org.blockchain.wallet.base.ResultResponse;
import org.blockchain.wallet.constant.ErrorMessage;
import org.blockchain.wallet.entity.User;
import org.blockchain.wallet.service.EmailService;
import org.blockchain.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

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

    @PostMapping(value = "/forgetPassword")
    public BaseResponse<User> forgetPassword(@RequestBody User user) {
        User userInfo = userService.forgetPassword(user);
        String subject = "重置密码";
        String text = "新密码：" + userInfo.getPassword() + "。请在登录后，前往个人信息页面修改密码";
        emailService.sendSimpleEmail(userInfo.getEmail(), subject, text);
        return new ResultResponse<>(userInfo);
    }
}
