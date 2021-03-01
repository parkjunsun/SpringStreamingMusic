package js.StreamingMusic.controller;

import js.StreamingMusic.exception.NotExistSearchResult;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.crawling.GetSearchSongs;
import js.StreamingMusic.service.data.DataApi;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final GetSearchSongs getSearchSongs;
    private final DataApi dataApi;


    @GetMapping("/search")
    public String showSearchSongs(Model model, @AuthenticationPrincipal MemberContext memberContext,
                                  @RequestParam(value = "keyword", required = false) String keyword,
                                  RedirectAttributes redirectAttributes,
                                  HttpServletRequest request) throws IOException {
        if(memberContext != null) {
            String username = memberContext.getUsername();
            model.addAttribute("name", username);
        }

        List<HashMap<String, String>> search = getSearchSongs.getSearch(keyword);
        if (!search.isEmpty()) {
            model.addAttribute("songs", search);
            model.addAttribute("keyword", keyword);
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "검색 결과가 없습니다.");
            return "redirect:" + request.getHeader("Referer");
        }

        return "search";
    }

    @PostMapping("/search")
    public String playSearchSong(Model model, @AuthenticationPrincipal MemberContext memberContext,
                                @RequestParam(value = "play") String param,
                                HttpServletRequest request) throws IOException, ParseException {

        if(memberContext != null) {
            String username = memberContext.getUsername();
            model.addAttribute("name", username);
        }

        String[] parameters = param.split(";");
        String title = parameters[0];
        String artist = parameters[1];
        Integer index = Integer.parseInt(parameters[2]);
        String keyword = parameters[3];

        List<String> videoIds = dataApi.getVideoId(title, artist);
        model.addAttribute("videoIds", videoIds);
        model.addAttribute("index", index);

        List<HashMap<String, String>> search = getSearchSongs.getSearch(keyword);
        model.addAttribute("songs", search);
        model.addAttribute("keyword", keyword);

        return "search";
//        return "redirect:" + request.getHeader("Referer");

    }



}
