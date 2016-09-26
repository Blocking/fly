package com.zhangxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/home").permitAll()
                .antMatchers("/file-upload").permitAll()
                .antMatchers("/**").permitAll()
                .antMatchers("/test").permitAll()
                .antMatchers("/users").permitAll()
                .antMatchers("/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**");
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/static/**");
        web.ignoring().antMatchers("/webjars/**");
    }

    //    @Autowired
    //    private DataSource dataSource;

    /*
     * @Autowired public void configureGlobal(AuthenticationManagerBuilder auth)
     * throws Exception { auth .inMemoryAuthentication()
     * .withUser("user").password("password").roles("USER"); auth
     * .jdbcAuthentication().dataSource(dataSource) .withDefaultSchema()
     * .withUser("user").password("password").roles("USER"); }
     */

    @Autowired
    private UserDetailsService userService;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(this.userService);
    }

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return this.userService;
    }

}
