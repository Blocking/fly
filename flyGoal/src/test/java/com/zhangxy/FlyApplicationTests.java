package com.zhangxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhangxy.entity.User;
import com.zhangxy.properties.TestProp;
import com.zhangxy.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlyApplicationTests {

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Test
    public void contextLoads() {
        final User user = new User();
        user.setUsername("admin");
        user.setPassword(this.passwordEncoder().encode("123456"));
        this.userRepository.save(user);
    }

    @Autowired
    private TestProp myProps;

    public void propsTest() throws JsonProcessingException {

        System.out.println("simpleProp: " + this.myProps.getSimpleProp());
        System.out.println("arrayProps: " + this.objectMapper.writeValueAsString(this.myProps.getArrayProps()));
        System.out.println("listProp1: " + this.objectMapper.writeValueAsString(this.myProps.getListProp1()));
        System.out.println("listProp2: " + this.objectMapper.writeValueAsString(this.myProps.getListProp2()));
        System.out.println("mapProps: " + this.objectMapper.writeValueAsString(this.myProps.getMapProps()));
    }

}
