package js.StreamingMusic.service.crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class GetTop10 {

    private static String genieTop10Url = "https://www.genie.co.kr/";


    public List<HashMap<String, String>> getTop10() throws IOException {

        List<HashMap<String, String>> top10 = new ArrayList<>();

        Document document = Jsoup.connect(genieTop10Url).get();
        Elements elements = document.select("div.music-list-wrap.main-chart-list table.list-wrap tbody tr.list");

        for (Element element : elements) {
            String img_src = element.selectFirst("td.info a.cover img").attr("src");
            String img = "https:" + img_src;
            String title = element.selectFirst("td.info > a.title").text();
            String artist = element.selectFirst("td.info > a.artist").text();
            String song_id = element.selectFirst("td.info > a.title").attr("onclick").split("'")[1];

            HashMap<String, String> map = new HashMap<>();
            map.put("img", img);
            map.put("title", title);
            map.put("artist", artist);
            map.put("song_id", song_id);

            top10.add(map);
        }

        return top10;
    }

}
