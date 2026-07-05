//package hu.fleetpulse.backend.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Minden endpointra vonatkozik
//                .allowedOrigins("http://localhost:5173") // A frontend címe
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true); // Fontos a WebSocket és session alapú kapcsolatokhoz
//    }
//}