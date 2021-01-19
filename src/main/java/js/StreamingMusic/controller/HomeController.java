package js.StreamingMusic.controller;

import js.StreamingMusic.service.crawling.GetTop10;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final GetTop10 songs;

    @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
    public String home(Model model, HttpServletRequest request) throws Exception{
        if (request.getMethod().equals("GET")) {
            model.addAttribute("top10", songs.getTop10());
        }
        else if (request.getMethod().equals("POST")) {
            model.addAttribute("top10", songs.getTop10());
            model.addAttribute("titleName", request.getParameter("top1_play"));
        }

        return "home";
    }

//    @PostMapping("/")
//    public String ListeningInHome(@RequestParam("top1_play") String top1, Model model) throws Exception{
//        model.addAttribute("top10", songs.getTop10());
//        model.addAttribute("titleName", top1);
//        return "redirect:/";
//    }


}
