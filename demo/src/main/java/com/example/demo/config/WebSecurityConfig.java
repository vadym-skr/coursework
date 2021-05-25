package com.example.demo.config;

import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

//це для того, щоб вказати що клас може бути використаний контейнером Spring IoC як конфігураційний клас для бінов.
@Configuration
// Це аннотація маркера. Це дозволяє Spring знаходити (це @Configuration, отже, @Component) і автоматично застосовувати клас до глобального WebSecurity.
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // AuthenticationProvider Реалізація, яка витягує інформацію про користувача з UserDetailsService.

    //DaoAuthenticationProvider- це простий провайдер аутентифікації, який використовує об'єкт доступу до даних (DAO) для добування інформації про користувача з реляційної бази даних. Він використовує UserDetailsService (як DAO) для пошуку імені користувача, пароля. Він аутентифікує користувача, просто порівнюючи пароль, представлений в файлі, з паролем, UsernamePasswordAuthenticationToken завантаженим в UserDetailsService.
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService());
        return authProvider;
    }

    @Override
    //HttpSecurity - дозволяє налаштувати безпеку на основі Інтернету для певних HTTP-запитів. За замовчуванням він буде застосовуватися до всіх запитів, але його можна обмежити за допомогою requestMatcher (RequestMatcher) або інших аналогічних методів.
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/user", "/user/registration", "/images/**", "/css/**","/admin/allUsers", "/objects/**").permitAll()
                .antMatchers("/user/**").hasAnyAuthority("USER", "ADMIN", "CREATOR")
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403")
        ;
    }

    @Override
    // AuthenticationManagerBuilder - SecurityBuilder використовується для створення файлу AuthenticationManager. Дозволяє легко вбудовувати аутентифікацію в пам'яті, аутентифікацію на основі JDBC, додавати UserDetailsService і додавати AuthenticationProvider.
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }



}
