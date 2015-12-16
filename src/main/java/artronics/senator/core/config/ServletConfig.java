package artronics.senator.core.config;

import org.h2.server.web.WebServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ServletConfig //extends SpringBootServletInitializer
{

//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
////        return builder.sources(Restbucks.class, ComponentConfiguration.class);
//    }

//    @Bean
//    public ServletRegistrationBean dispatcherServletRegistration() {
//        ServletRegistrationBean registration = new ServletRegistrationBean();
//        Map<String,String> params = new HashMap<String,String>();
//        params.put("mvc-dispatcher","org.springframework.web.servlet.DispatcherServlet");
//        params.put("contextClass","org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
//        params.put("contextConfigLocation","classpath:senator-beans.xml");
//        registration.setInitParameters(params);
//        return registration;
//    }
//
//}

    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }
}
