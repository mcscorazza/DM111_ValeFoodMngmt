package br.inatel.pos.dm11.vfu.config;

import br.inatel.pos.dm11.vfu.api.core.Interceptor.AuthenticationInterceptor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public class AppInterceptorRegistry implements WebMvcConfigurer {
    private static final List<String> ENDPOINTS_PATTERN = List.of(
            "/valefood/users/**",
            "/valefood/users**",
            "/valefood/restaurants**",
            "/valefood/restaurants/**"
    );

    private final AuthenticationInterceptor interceptor;

    public AppInterceptorRegistry(AuthenticationInterceptor interceptor){
        this.interceptor = interceptor;
    }

}
