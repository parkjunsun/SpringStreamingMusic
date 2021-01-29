package js.StreamingMusic.controller;

import js.StreamingMusic.exception.NotExistSearchResult;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.crawling.GetSearchSongs;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final GetSearchSongs getSearchSongs;

    @GetMapping("/search")
    public String showSearchSongs(Model model, @AuthenticationPrincipal MemberContext memberContext,
                                  @RequestParam(value = "search", required = false) String keyword,
                                  RedirectAttributes redirectAttributes) throws IOException {
        if(memberContext != null) {
            String username = memberContext.getUsername();
            model.addAttribute("name", username);
        }

        List<HashMap<String, String>> search = getSearchSongs.getSearch(keyword);
        if (!search.isEmpty()) {
            model.addAttribute("songs", search);
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "검색 결과가 없습니다.");
            return "redirect:/";
        }


        return "search";
    }



}
