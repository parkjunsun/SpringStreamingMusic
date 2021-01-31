package js.StreamingMusic.controller;

import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.crawling.GetTop200;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChartController {

    private final GetTop200 getTop200;
    private final DataApi dataApi;

    @GetMapping("/chart/top200/{pgNum}")
    public String showTop200(Model model, @PathVariable("pgNum") String pgNum, @AuthenticationPrincipal MemberContext memberContext) throws IOException {
        if(memberContext != null) {
            String username = memberContext.getUsername();
            model.addAttribute("name", username);
        }

        List<HashMap<String, String>> songs = getTop200.getSongs(pgNum);
        model.addAttribute("songs", songs);
        model.addAttribute("pgNum", pgNum);

        return "topchart" + pgNum;
    }

    @PostMapping("/chart/top200/{pgNum}")
    public String playTop200(Model model, @PathVariable("pgNum") String pgNum,
                             @AuthenticationPrincipal MemberContext memberContext,
                             @RequestParam(value = "play") String param) throws IOException, ParseException {
        if(memberContext != null) {
            String username = memberContext.getUsername();
            model.addAttribute("name", username);
        }

        String[] parameters = param.split(";");
        String title = parameters[0];
        String artist = parameters[1];
        Integer index = Integer.parseInt(parameters[2]);

        List<String> videoIds = dataApi.getVideoId(title, artist);
        model.addAttribute("videoIds", videoIds);
        model.addAttribute("index", index);

        List<HashMap<String, String>> songs = getTop200.getSongs(pgNum);
        model.addAttribute("songs", songs);

        return "topchart" + pgNum;
    }
}
