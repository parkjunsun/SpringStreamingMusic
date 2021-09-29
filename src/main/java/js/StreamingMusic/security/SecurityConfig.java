package js.StreamingMusic.security;


import js.StreamingMusic.oauth.PrincipalOauth2UserService;
import js.StreamingMusic.util.ConfigUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import javax.sql.DataSource;



@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록됨
//@EnableGlobalMethodSecurity(securedEnabled = true) //secured 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final DataSource dataSource;
    private final PrincipalOauth2UserService principalOauth2UserService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();   // 평문인 비밀번호 암호화
    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        return repository;
    }


    @Override
    public void configure(WebSecurity web){
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers("/favicon.ico", "/resources/**", "/error");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .rememberMe()
                .key("rememberMe")
                .userDetailsService(userDetailsService)
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60*60*24);
        http
                .authorizeRequests()
                .antMatchers("/", "/oauth2/**", "/register","/login", "/login/**", "/ajax", "/ajax/**", "/detail", "/detail/**", "/search", "/chart", "/chart/**", "/forget", "/forget/**").permitAll()
                .antMatchers("/playlist","/youtube").hasRole("USER")
                .antMatchers("/admin", "/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
        .and()
                .formLogin()
                .loginPage("/login") //사용자 정의 loginPage
                .loginProcessingUrl("/login_proc") ///login_proc 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줌
                .defaultSuccessUrl("/")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()
        .and()
                .oauth2Login() //구글 로그인이 완료된 뒤의 후처리가 필요 1.코드받기(인증됬다는것) 2.엑세스토큰(권한) 3.사용자프로필 정보를 가져옴 4-1.그 정보를 토대로 회원가입 자동진행 4-2. 집주소, vip(일반)등급같은 추가할 정보가 있을 경우 또 입력이 가능해야함
                                //중요! 구글 로그인이 완료가 되면 엑세스 토큰 + 사용자프로필 정보를 받음
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
    }

}
