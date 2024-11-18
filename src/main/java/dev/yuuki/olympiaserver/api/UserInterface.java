package dev.yuuki.olympiaserver.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import dev.yuuki.olympiaserver.services.Authorization;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class UserInterface {

    private final Authorization authorization;

    public UserInterface(Authorization authorization) {
        this.authorization = authorization;
    }
    
    @GetMapping("/")
    public RedirectView getMethodName(HttpServletRequest request) {
        String token = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authorization")) {
                token = cookie.getValue();
                break;
            }
        }
        try {
            switch (authorization.validateUser(token).role) {
                case ADMINISTRATOR: return new RedirectView("./admin.html");
                case CONTESTANT: return new RedirectView("./contestant.html");
                case VIEWER: return new RedirectView("./viewer.html");
                default: return new RedirectView("./login.html");
            }
        } catch (Exception ignored) {
            return new RedirectView("./login.html");
        }
    }
    
}
