package com.zhangxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhangxy.properties.TestProp;
import com.zhangxy.repository.CustomerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlyApplicationTests {
	
	protected final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void contextLoads() {
        this.customerRepository.findAll().forEach(p -> {
            System.out.println(p.getId());
        });
    }
    
    @Autowired  
    private TestProp myProps;   
      
    @Test  
    public void propsTest() throws JsonProcessingException { 
    	
        System.out.println("simpleProp: " + myProps.getSimpleProp());  
        System.out.println("arrayProps: " + objectMapper.writeValueAsString(myProps.getArrayProps()));  
        System.out.println("listProp1: " + objectMapper.writeValueAsString(myProps.getListProp1()));  
        System.out.println("listProp2: " + objectMapper.writeValueAsString(myProps.getListProp2()));  
        System.out.println("mapProps: " + objectMapper.writeValueAsString(myProps.getMapProps()));  
    }  

}
