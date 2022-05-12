package com.jw.userservice.config;

import com.jw.userservice.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity
                .csrf().disable()
                .formLogin()
                    .usernameParameter("email")
                .and()
                    .logout()
                .and()
                    .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
<<<<<<< Updated upstream
                        .antMatchers("/register/**", "/h2-console/**").permitAll()
=======
                        .antMatchers("/register", "/h2-console/**").permitAll()
>>>>>>> Stashed changes
                        .anyRequest().authenticated();
    }
}
