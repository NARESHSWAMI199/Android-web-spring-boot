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
                "/store/all",
                "/store/detail/**",
                "/store/-detail/**",
                "/store/image/**",
                "/store/categories",
                "/store/categories/**",
                "/store/subcategory",
                "/store/subcategory/**",
                "/store/ratings/**",
                "/item/all",
                "/item/detail/**",
                "/item/image/**",
                "/item/categories",
                "/item/categories/**",
                "/item/subcategory",
                "/item/subcategory/**",
                "/item/ratings/**",
                "/slips/pdf/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/api-docs/**",
                "/auth/login",
                "/auth/register",
                "/review/all",
                "/review/detail/**",
                "/auth/profile/**",
                "/paytm/**"
        };
        registry.addInterceptor(new SalesInterceptor(jwtToken,userRepository))
                .excludePathPatterns(excludedPaths);
    }
}
