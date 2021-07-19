package js.StreamingMusic.controller.chart;

import js.StreamingMusic.domain.entity.Member;
//import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.crawling.GetTop200;
import js.StreamingMusic.service.data.DataApi;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class TopChartController {

    private final GetTop200 getTop200;
    private final DataApi dataApi;

    @GetMapping("/chart/top200/{pgNum}")
    public String showTop200(Model model, @PathVariable("pgNum") String pgNum,
                             @AuthenticationPrincipal MemberContext memberContext) throws IOException {


        if (memberContext != null) {
            if (!memberContext.getMember().getProvider().equals("JSMUSIC")) model.addAttribute("name", memberContext.getMember().getRealname());
            else model.addAttribute("name", memberContext.getMember().getUsername());

            model.addAttribute("social", memberContext.getMember().getProvider());
        }

        List<HashMap<String, String>> songs = getTop200.getSongs(pgNum);

        model.addAttribute("songs", songs);
        model.addAttribute("pgNum", pgNum);

        return "chart/topchart" + pgNum;
    }

    @PostMapping("/chart/top200/{pgNum}")
    public String playTop200(Model model, @PathVariable("pgNum") String pgNum,
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
        List<HashMap<String, String>> songs = getTop200.getSongs(pgNum);

        redirectAttributes.addFlashAttribute("videoIds", videoIds);
        redirectAttributes.addFlashAttribute("index", index);
        redirectAttributes.addFlashAttribute("songs", songs);
        //PRG - Post/Redirect/Get

        redirectAttributes.addAttribute("pgNum", pgNum);

        return "redirect:/chart/top200/{pgNum}";
    }
}
