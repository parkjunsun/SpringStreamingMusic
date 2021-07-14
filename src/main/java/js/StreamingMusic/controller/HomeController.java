package js.StreamingMusic.controller;


import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.crawling.GetHomeNewAlbums;
import js.StreamingMusic.service.crawling.GetTop10;
import js.StreamingMusic.service.data.DataApi;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final GetTop10 songs;
    private final GetHomeNewAlbums getHomeNewAlbums;
    private final DataApi dataApi;
    private final HttpSession httpSession;

    @GetMapping("/test/login") //@AuthenticationPrincipal: 세션정보에 접근
    public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal MemberContext memberContext) {
        MemberContext principalDetails = (MemberContext) authentication.getPrincipal();
        System.out.println("authentication:" + principalDetails.getMember());
        System.out.println("userDetails:" + memberContext.getMember());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login") //@AuthenticationPrincipal: 세션정보에 접근
    public @ResponseBody String OauthloginTest(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication:" + oAuth2User.getAttributes());
        System.out.println("oauth2User:" + oauth.getAttributes());
        return "Oauth세션 정보 확인하기";
    }


    @GetMapping("/")
    public String ShowHome(Model model, @AuthenticationPrincipal MemberContext member) throws IOException {

        if (member != null) {
            String username = member.getUsername();
            model.addAttribute("name", username);
        }

        model.addAttribute("top10", songs.getTop10());
        model.addAttribute("albums", getHomeNewAlbums.getHomeNewAlbumPg1());

        return "home";
    }

    @PostMapping("/")
    public String ListenHome(
            @AuthenticationPrincipal MemberContext member,
            RedirectAttributes redirectAttributes, HttpServletRequest request) throws IOException, ParseException {


        if (member != null) {
            String username = member.getUsername();
            redirectAttributes.addFlashAttribute("name", username);
        }

        String param = request.getParameter("play");
        String[] data = param.split(";");
        String title = data[0];
        String artist = data[1];
        Integer index = Integer.parseInt(data[2]);


        List<String> videoIds = dataApi.getVideoId(title, artist);

        redirectAttributes.addFlashAttribute("videoIds", videoIds);
        redirectAttributes.addFlashAttribute("index", index);
        redirectAttributes.addFlashAttribute("top10", songs.getTop10());
        redirectAttributes.addFlashAttribute("albums", getHomeNewAlbums.getHomeNewAlbumPg1());
        //PRG - Post/Redirect/Get

        return "redirect:/";

    }

}
