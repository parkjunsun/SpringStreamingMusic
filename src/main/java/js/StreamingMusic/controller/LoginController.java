package js.StreamingMusic.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @GetMapping("/login")      //로그인 에러시 ex url) /login?error=true&exception=Invalid Username or Password
    public String login(@RequestParam(value = "error", required = false) String error, //required = false는 항상 이 값이 넘어오는게 아니기 때문에 설정
                        @RequestParam(value = "exception", required = false) String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }

        return "redirect:/login";
    }
}
