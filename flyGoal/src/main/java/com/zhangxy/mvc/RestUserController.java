package com.zhangxy.mvc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zhangxy.entity.User;
import com.zhangxy.service.UsersService;


@RestController
public class RestUserController {
	
	@Autowired
	private UsersService userservice;
	
	@RequestMapping(value="users",method=RequestMethod.GET)
	public List<User> getUsers(){
		List<User> users = userservice.getAll();
		return users;
	}
	
	@RequestMapping(value="register",method=RequestMethod.POST)
	public User register(@ModelAttribute User user){
		User users = userservice.save(user);
		return users;
	}
	
}
