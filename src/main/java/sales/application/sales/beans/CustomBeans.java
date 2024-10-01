package sales.application.sales.beans;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;


@Configuration
public class CustomBeans {

    @Bean
    public Logger getLogger(){
        return Logger.getGlobal();
    }

}
