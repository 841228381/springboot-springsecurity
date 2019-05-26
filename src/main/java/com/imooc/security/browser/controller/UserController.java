package com.imooc.security.browser.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.imooc.security.browser.dto.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XZP on 2019/5/25.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 获取当前登陆信息
     * @param user
     * @return
     */
    @GetMapping("/me")
    public Object getCurrentUser(@AuthenticationPrincipal UserDetails user) {
        return user;
    }

    @GetMapping
    @JsonView(User.UserSimpleView.class)
    @ApiOperation(value = "用户查询服务")
    public List<User> query() {
        List<User> users = new ArrayList<User>();
        users.add(new User());
        users.add(new User());
        users.add(new User());
        return users;
    }
}
