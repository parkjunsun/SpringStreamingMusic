package js.StreamingMusic.controller.chart;

import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.crawling.GetNewestSongs;
import js.StreamingMusic.service.data.DataApi;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NewChartController {

    private final GetNewestSongs getNewestSongs;
    private final DataApi dataApi;

    @GetMapping("/chart/new/{pgNum}")
    public String showNewSongs(Model model, @PathVariable("pgNum") String pgNum,
                               @AuthenticationPrincipal MemberContext memberContext) throws IOException {

        if(memberContext != null) {
            String username = memberContext.getUsername();
            model.addAttribute("name", username);
        }

        List<HashMap<String, String>> songs = getNewestSongs.getSongs(pgNum);
        model.addAttribute("songs", songs);
        model.addAttribute("pgNum", pgNum);

        return "chart/newchart" + pgNum;
    }

    @PostMapping("/chart/new/{pgNum}")
    public String playNewSong(Model model, @PathVariable("pgNum") String pgNum,
                              @AuthenticationPrincipal MemberContext memberContext,
                              @RequestParam(value = "play") String param,
                              RedirectAttributes redirectAttributes) throws IOException, ParseException {

        if(memberContext != null) {
            String username = memberContext.getUsername();
            redirectAttributes.addFlashAttribute("name", username);
        }

        String[] parameters = param.split(";");
        String title = parameters[0];
        String artist = parameters[1];
        Integer index = Integer.parseInt(parameters[2]);

        List<String> videoIds = dataApi.getVideoId(title, artist);
        List<HashMap<String, String>> songs = getNewestSongs.getSongs(pgNum);

        redirectAttributes.addFlashAttribute("videoIds", videoIds);
        redirectAttributes.addFlashAttribute("index", index);
        redirectAttributes.addFlashAttribute("songs", songs);
        //PRG - Post/Redirect/Get

        redirectAttributes.addAttribute("pgNum", pgNum);

        return "redirect:/chart/new/{pgNum}";
    }

}
