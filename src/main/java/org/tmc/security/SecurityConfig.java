package org.tmc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConverter converter;

    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.cors();

        http.authorizeRequests()
                .antMatchers( HttpMethod.POST, "/api/security/authenticate" ).permitAll()
                .antMatchers( HttpMethod.POST,"/api/security/create_account").permitAll()
                .antMatchers( HttpMethod.GET, "/api/user", "/api/user/*").permitAll()
                .antMatchers( HttpMethod.GET, "/api/user/name/*").authenticated()
                .antMatchers( HttpMethod.GET, "/api/recipe/user/*").permitAll()
                .antMatchers( HttpMethod.GET, "/api/recipe/cuisine/*").permitAll()
                .antMatchers( HttpMethod.GET, "/api/recipe", "/api/recipe/*").permitAll()
                .antMatchers( HttpMethod.GET, "/api/recipe/name/*").permitAll()
                .antMatchers( HttpMethod.GET, "/api/cuisine", "/api/cuisine/*").permitAll()
                .antMatchers( HttpMethod.GET, "/api/ingredient", "/api/ingredient/*").permitAll()
                .antMatchers( HttpMethod.GET, "/api/ingredient/recipe/*").permitAll()
                .antMatchers( HttpMethod.POST, "/api/recipe" ).permitAll()
                .antMatchers( HttpMethod.POST, "/api/ingredient" ).permitAll()
                .antMatchers( HttpMethod.PUT, "/api/recipe/*" ).permitAll()
                .antMatchers( HttpMethod.PUT, "/api/ingredient/*" ).permitAll()
                .antMatchers( HttpMethod.PUT, "/api/user/*" ).authenticated()
                .antMatchers( HttpMethod.DELETE, "/api/recipe/*").permitAll()
                .antMatchers( HttpMethod.DELETE, "/api/ingredient/*").permitAll()
                .antMatchers( "/**" ).denyAll()
                .and()
                .addFilter(new JwtRequestFilter(authenticationManager(), converter))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
  
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*");
            }
        };
    }
}
