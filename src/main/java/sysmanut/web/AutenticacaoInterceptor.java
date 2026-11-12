package sysmanut.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** RNF04: páginas protegidas exigem login (sessão). */
@Configuration
public class AutenticacaoInterceptor implements WebMvcConfigurer, HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object h) throws Exception {
        if (req.getSession().getAttribute("usuario") != null) return true;
        res.sendRedirect("/login");
        return false;
    }
    @Override
    public void addInterceptors(InterceptorRegistry r) {
        r.addInterceptor(this).excludePathPatterns("/login", "/css/**", "/js/**", "/error");
    }
}
