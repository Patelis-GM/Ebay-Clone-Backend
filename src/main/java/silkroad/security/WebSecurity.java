package silkroad.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import silkroad.entities.Roles;
import silkroad.services.UserService;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/admin/**").hasAuthority(Roles.ADMIN.toString())
//                .antMatchers(HttpMethod.GET, "/admin/**").hasAuthority(Roles.ADMIN.toString())
                .antMatchers("/**").permitAll()
                .antMatchers(HttpMethod.GET, "/approved").hasAnyAuthority(Roles.ADMIN.toString(), Roles.USER.toString())
                .antMatchers("/auctions/check").permitAll()
                .antMatchers("/auctions/**").hasAnyAuthority(Roles.ADMIN.toString(), Roles.USER.toString())
                .antMatchers(HttpMethod.GET, "/admin").hasAnyAuthority(Roles.ADMIN.toString())
                .antMatchers(HttpMethod.POST, "/sign-up").permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManagerBean()))
                .addFilterBefore(new JWTAuthorizationFilter(authenticationManagerBean(), this.userService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /* Build Authentication Manager */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(this.userService).passwordEncoder(this.bCryptPasswordEncoder);
    }

    /* Retrieve Authentication Manager */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(64000);
        return loggingFilter;
    }


}
