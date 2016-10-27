package com.zhangxy.controllor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhangxy.entity.User;
import com.zhangxy.service.UsersService;

@RestController
public class RestUserController {

    @Autowired
    private UsersService userservice;

    @RequestMapping(value = "users", method = RequestMethod.GET)
    public List<User> getUsers() {
        final List<User> users = this.userservice.getAll();
        return users;
    }

    @RequestMapping(value = "getUser", method = RequestMethod.GET)
    public User getUserByName(@RequestParam(value = "name") final String name) {
        final User users = this.userservice.getUserByUserName(name);
        return users;
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public User register(@ModelAttribute final User user) {
        final User users = this.userservice.save(user);
        return users;
    }

    @RequestMapping(value = "registers", method = RequestMethod.GET)
    public User registers(@RequestParam(value = "username") final String username,
            @RequestParam(value = "password") final String password) {
        final User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        final User users = this.userservice.save(user);
        return users;
    }

}
