package js.StreamingMusic.controller;

import js.StreamingMusic.security.MemberContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlaylistController {

    @GetMapping("/playlist")
    public String playlist(Model model, Authentication authentication) {
        MemberContext ac = (MemberContext) authentication.getPrincipal();
        model.addAttribute("info", ac.getUsername());

        return "playlist";
    }
}
