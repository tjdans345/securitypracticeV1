package com.security.securitypracticev1.security;


import com.security.securitypracticev1.MemberService;
import com.security.securitypracticev1.security.auth.SecurityService;
import com.security.securitypracticev1.security.custom.AuthFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 시킵니다.
@EnableGlobalMethodSecurity(prePostEnabled = true) //특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 뜻.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private SecurityService securityService;
    private AuthFailureHandler authFailureHandler;

    @Autowired
    public SecurityConfig(SecurityService securityService, AuthFailureHandler authFailureHandler) {
        this.securityService = securityService;
        this.authFailureHandler = authFailureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 이미지 , 자바스크립트 , css 디렉토리 보안 설정
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
    }

    /**
     * HTTP 관련 보안 설정 <가장중요>
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/member/**").authenticated()
                .antMatchers("/admin/**").authenticated()
                .antMatchers("/**").permitAll();

        http.formLogin()
                .loginPage("/v1/security/login")
                .defaultSuccessUrl("/home")
                .failureHandler(authFailureHandler)
                .permitAll();

        /**
         * logoutSuccessUrl("/path")
         * 로그아웃 성공 시 이동할 경로를 지정합니다.
         * invalidateHttpSession(true)
         * 로그아웃 성공 시 세션을 제거합니다.
         */
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/v1/security/login")
                .invalidateHttpSession(true);

        /**
         * 권한 없는 사용자가 접근했을 경우 이동할 경로를 지정하는 부분
         */
        http.exceptionHandling()
                .accessDeniedPage("/v1/security/login");
    }

    /**
     * 실제 인증을 진행할 Provider
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(securityService).passwordEncoder(passwordEncoder());
    }






}
