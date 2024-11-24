package sales.application.sales.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;
import sales.application.sales.dto.ErrorDto;
import sales.application.sales.entities.User;
import sales.application.sales.jwtUtils.JwtToken;
import sales.application.sales.repostories.UserRepository;

import java.io.IOException;

public class SalesInterceptor implements HandlerInterceptor {
    JwtToken jwtToken;
    UserRepository userRepository;


    public SalesInterceptor(JwtToken jwtToken, UserRepository userRepository){
        this.jwtToken = jwtToken;
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("request url : "+request.getRequestURI());
        try {
            if (token != null && (token).startsWith("Bearer ")) {
                token = token.substring(7);
                String slug = jwtToken.getSlugFromToken(token);
                /* get user by slug. */
                User user = userRepository.findUserBySlug(slug);
                request.setAttribute("user",user);
                return true;
            }
            sendError(response,"Invalid authorization.",401);
            return false;
        }catch (Exception e){
            sendError(response,e.getMessage(),401);
            return false;
        }
    }

    public void sendError(HttpServletResponse response ,String message, Integer status) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setStatus(status);
        ErrorDto error = new ErrorDto(message,status);
        response.getWriter().write(mapper.writeValueAsString(error));
    }

}



