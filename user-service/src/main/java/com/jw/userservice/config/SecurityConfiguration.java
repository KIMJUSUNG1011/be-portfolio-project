package com.jw.userservice.config;

import com.jw.userservice.UserService;
import com.jw.userservice.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
    private final Environment env;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity.csrf().disable();

        httpSecurity
                .formLogin().usernameParameter("email")
                .and()
                    .logout()
                .and()
                    .authorizeRequests()
                        .antMatchers("/register/**", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                    .antMatcher("/**").addFilter(getAuthenticationFilter());

        httpSecurity.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception
    {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), userService, env);

        return authenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }
}
