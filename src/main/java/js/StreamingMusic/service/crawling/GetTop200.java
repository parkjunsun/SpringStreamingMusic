package js.StreamingMusic.service.crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.*;

@Service
public class GetTop200 {

    public List<HashMap<String, String>> getSongs(String page) throws IOException {
        int cnt = 1;
        List<HashMap<String, String>> songs = new ArrayList<>();

        Date d = new Date();
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat h = new SimpleDateFormat("HH");

        String today = day.format(d);
        String hour = h.format(d);

        String url = String.format("https://www.genie.co.kr/chart/top200?dict=D&ymd=%s&hh=%s&rtm=Y&pg=%s", today, hour, page);

        Document doc = Jsoup.connect(url).get();
        Elements trs = doc.select("tbody > tr.list");

        for (Element tr : trs) {
            String song_id = tr.attr("songid");
            Element info = tr.selectFirst("td.info");
            String title = info.selectFirst("a.title").text().strip();
            String artist = info.selectFirst("a.artist").text().strip();

            Elements td = tr.select("td");
            Element data = td.get(2);
            Element img_src = data.selectFirst("img");
            String img = "https:" + img_src.attr("src");

            HashMap<String, String> map = new HashMap<>();
            map.put("title", title);
            map.put("artist", artist);
            map.put("img", img);
            map.put("song_id", song_id);

            if (page.equals("2")) {
                map.put("index", Integer.toString(cnt+50));
            } else if (page.equals("3")) {
                map.put("index", Integer.toString(cnt+100));
            } else if (page.equals("4")) {
                map.put("index", Integer.toString(cnt+150));
            }

            songs.add(map);
            cnt += 1;

        }

        return songs;
    }


}
