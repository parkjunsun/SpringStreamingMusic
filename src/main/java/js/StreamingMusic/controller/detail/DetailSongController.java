package js.StreamingMusic.controller.detail;

import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.crawling.GetDetailSongInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class DetailSongController {

    private final GetDetailSongInfo getDetailSongInfo;

    @GetMapping("/detail/songinfo")
    public String songinfo(Model model, @RequestParam("song_id") String song_id) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        String name = "";
        if (principal != null && principal instanceof MemberContext) {
            name = ((MemberContext) principal).getUsername();
            model.addAttribute("name", name);
        }
        HashMap<String, String> info = getDetailSongInfo.getData(song_id);
        model.addAttribute("info", info);

        return "detail/songinfo";
    }
}
