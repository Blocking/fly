package com.zhangxy.mvc;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhangxy.entity.Customer;
import com.zhangxy.entity.User;
import com.zhangxy.repository.CustomerRepository;
import com.zhangxy.service.CustomerService;


@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    @Autowired private  CustomerRepository cr;
    @Autowired private CustomerService  cs;
    
    @Autowired
    private JavaMailSenderImpl mailSender;

    @RequestMapping("/greetings")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
    	List<Customer> list = cr.findByLastName(name);
    	StringBuilder sb = new StringBuilder();
    	list.forEach(p->{
    		sb.append(p.getId()+"::");
    	});
    	sb.append(name);
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, sb));
    }
    @RequestMapping("/test")
    public String test(){
    	cs.findAll();
    	return "sussess";
    }
    @RequestMapping("testMail")
    public User name() {
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	MimeMessage message  = mailSender.createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(message);
    	try {
    		String[] arr = {"516989510@qq.com","1223288714@qq.com"};
    		helper.setTo(arr);
			helper.setFrom("18510855440@sina.cn");
			helper.setText("<h1>你大爷的！</h1>", true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
    	mailSender.send(message);
		return user;
	}
    
}