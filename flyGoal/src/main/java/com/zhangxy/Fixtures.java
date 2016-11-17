package com.zhangxy;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class Fixtures implements Ordered, ApplicationListener<ContextRefreshedEvent> {

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        configure();
    }

    public void testMail() {
        System.out.println("ss");
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.sina.cn");
        mailSender.setPort(25);
        mailSender.setUsername("12");
        mailSender.setPassword("12");
        try {
            mailSender.testConnection();
        } catch (final MessagingException e) {
            e.printStackTrace();
        }
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom("18510855440@sina.cn");
            helper.setTo("1223288714@qq.com");
            helper.setText("Thank you for ordering!");
        } catch (final MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(message);
    }

    private void configure() {
        System.out.println("====================================");
        /*
         * System.out.println("====================================");
         * System.out.println(mailSender.getHost()); MimeMessage message =
         * mailSender.createMimeMessage(); MimeMessageHelper helper = new
         * MimeMessageHelper(message); try {
         * helper.setFrom("18510855440@sina.cn");
         * helper.setTo("1223288714@qq.com");
         * helper.setText("Thank you for ordering!"); } catch
         * (MessagingException e) { e.printStackTrace(); }
         * mailSender.send(message);
         */
        //        final MovieGrabInfo info = new MovieGrabInfo();
        //        info.setName("超人");
        //        info.setWriter("阿迪王");
        //        this.movieGrabInfoRepository.save(info);
        //
        //        final MovieCode code = new MovieCode();
        //        code.setFilmCode("0001");
        //        code.setFilmName("超人前传");
        //        code.setMovieGrabInfo(info);
        //        this.movieCodeRepository.save(code);
        //
        //        final MovieCode code1 = new MovieCode();
        //        code1.setFilmCode("0002");
        //        code1.setFilmName("超人后传");
        //        code1.setMovieGrabInfo(info);
        //        this.movieCodeRepository.save(code1);
    }
}
