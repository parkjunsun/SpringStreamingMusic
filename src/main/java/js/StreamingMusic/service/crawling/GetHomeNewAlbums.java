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
public class GetHomeNewAlbums {

    private static String url = "https://www.genie.co.kr/";

    public List<Element> base() throws IOException {
        List<Element> info = new ArrayList<>();

        Document document = Jsoup.connect(url).get();
        Elements elements = document.select("div.slide-wrapper.active ul.bxslider li");

        for (Element element : elements) {
            Elements select = element.select("ul.list-album li");
            for (Element element1 : select) {
                info.add(element1);
            }
        }

        return info;
    }

    public List<HashMap<String, String>> getHomeNewAlbumPg1() throws IOException {
        List<HashMap<String, String>> pg1_info = new ArrayList<>();
        List<Element> pg1 = new ArrayList<>();
        List<Element> base = base();

        for (int i = 0; i < 12; i++) {
            pg1.add(base.get(i));
        }

        for (Element e : pg1) {
            String album_id = e.attr("album_id");
            String img_src = e.selectFirst("div.cover img").attr("src");
            String img = "https:" + img_src;
            String title = e.selectFirst("div.info-album a.album-title").text();
            String artist = e.selectFirst("div.info-album a.artist").text();

            HashMap<String, String> map = new HashMap<>();
            map.put("img", img);
            map.put("title", title);
            map.put("artist", artist);
            map.put("album_id", album_id);

            pg1_info.add(map);
        }

        return pg1_info;
    }

    public List<HashMap<String, String>> getHomeNewAlbumPg2() throws IOException {
        List<HashMap<String, String>> pg2_info = new ArrayList<>();
        List<Element> pg2 = new ArrayList<>();
        List<Element> base = base();

        for (int i = 12; i < 24; i++) {
            pg2.add(base.get(i));
        }

        for (Element e : pg2) {
            String album_id = e.attr("album_id");
            String img_src = e.selectFirst("div.cover img").attr("src");
            String img = "https:" + img_src;
            String title = e.selectFirst("div.info-album a.album-title").text();
            String artist = e.selectFirst("div.info-album a.artist").text();

            HashMap<String, String> map = new HashMap<>();
            map.put("img", img);
            map.put("title", title);
            map.put("artist", artist);
            map.put("album_id", album_id);

            pg2_info.add(map);
        }

        return pg2_info;
    }

    public List<HashMap<String, String>> getHomeNewAlbumPg3() throws IOException {
        List<HashMap<String, String>> pg3_info = new ArrayList<>();
        List<Element> pg3 = new ArrayList<>();
        List<Element> base = base();

        for (int i = 24; i < 36; i++) {
            pg3.add(base.get(i));
        }

        for (Element e : pg3) {
            String album_id = e.attr("album_id");
            String img_src = e.selectFirst("div.cover img").attr("src");
            String img = "https:" + img_src;
            String title = e.selectFirst("div.info-album a.album-title").text();
            String artist = e.selectFirst("div.info-album a.artist").text();

            HashMap<String, String> map = new HashMap<>();
            map.put("img", img);
            map.put("title", title);
            map.put("artist", artist);
            map.put("album_id", album_id);

            pg3_info.add(map);
        }

        return pg3_info;
    }

    public List<HashMap<String, String>> getHomeNewAlbumPg4() throws IOException {
        List<HashMap<String, String>> pg4_info = new ArrayList<>();
        List<Element> pg4 = new ArrayList<>();
        List<Element> base = base();

        for (int i = 36; i < 48; i++) {
            pg4.add(base.get(i));
        }

        for (Element e : pg4) {
            String album_id = e.attr("album_id");
            String img_src = e.selectFirst("div.cover img").attr("src");
            String img = "https:" + img_src;
            String title = e.selectFirst("div.info-album a.album-title").text();
            String artist = e.selectFirst("div.info-album a.artist").text();

            HashMap<String, String> map = new HashMap<>();
            map.put("img", img);
            map.put("title", title);
            map.put("artist", artist);
            map.put("album_id", album_id);

            pg4_info.add(map);
        }

        return pg4_info;
    }

    public List<HashMap<String, String>> getHomeNewAlbumPg5() throws IOException {
        List<HashMap<String, String>> pg5_info = new ArrayList<>();
        List<Element> pg5 = new ArrayList<>();
        List<Element> base = base();

        for (int i = 48; i < 60; i++) {
            pg5.add(base.get(i));
        }


        for (Element e : pg5) {
            String album_id = e.attr("album_id");
            String img_src = e.selectFirst("div.cover img").attr("src");
            String img = "https:" + img_src;
            String title = e.selectFirst("div.info-album a.album-title").text();
            String artist = e.selectFirst("div.info-album a.artist").text();

            HashMap<String, String> map = new HashMap<>();
            map.put("img", img);
            map.put("title", title);
            map.put("artist", artist);
            map.put("album_id", album_id);

            pg5_info.add(map);
        }

        return pg5_info;
    }

}
