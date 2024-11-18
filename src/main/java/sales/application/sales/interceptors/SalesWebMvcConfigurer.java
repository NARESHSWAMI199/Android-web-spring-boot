package sales.application.sales.interceptors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sales.application.sales.jwtUtils.JwtToken;
import sales.application.sales.repostories.UserRepository;

@Configuration
@EnableWebMvc
public class SalesWebMvcConfigurer implements WebMvcConfigurer {


    @Autowired
    JwtToken jwtToken;
    @Autowired
    UserRepository userRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludedPaths = {
                "/store/**",
                "/item/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/api-docs/**",
                "/auth/login",
                "/auth/register"
        };
        registry.addInterceptor(new SalesInterceptor(jwtToken,userRepository))
                .excludePathPatterns(excludedPaths);
    }
}
