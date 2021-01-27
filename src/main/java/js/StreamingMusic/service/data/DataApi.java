package js.StreamingMusic.service.data;

import com.google.common.net.UrlEscapers;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataApi {

    private static String api_key = "AIzaSyD26KoGfRFz3p6ptxqInXSGKq3jt8_C13I";

    public List<String> getVideoId(String title, String artist) throws IOException, MalformedURLException, ParseException {

        List<String> videoIds = new ArrayList<>();

        String urlStr = "https://www.googleapis.com/youtube/v3/search?q=" + title + "+" + artist + "+" + "audio&key=" + api_key +"&part=id&maxResults=3&type=video&videoEmbeddable=true";
        String encodedUrl = UrlEscapers.urlFragmentEscaper().escape(urlStr);
        URL url = new URL(encodedUrl);

        BufferedReader bf;
        String line = "";
        String result = "";

        bf = new BufferedReader(new InputStreamReader(url.openStream()));

        while((line = bf.readLine()) != null) {
            result = result.concat(line);
        }

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(result);

        JSONArray items = (JSONArray) obj.get("items");
        int dataCnt = items.size();

        if (dataCnt == 1) {
            JSONObject info1 = (JSONObject) items.get(0);
            JSONObject id1 = (JSONObject) info1.get("id");

            String videoId1 = (String) id1.get("videoId");
            String videoId2 = "";
            String videoId3 = "";

            videoIds.add(videoId1);
            videoIds.add(videoId2);
            videoIds.add(videoId3);
        } else if (dataCnt == 2) {
            JSONObject info1 = (JSONObject) items.get(0);
            JSONObject id1 = (JSONObject) info1.get("id");
            JSONObject info2 = (JSONObject) items.get(1);
            JSONObject id2 = (JSONObject) info2.get("id");

            String videoId1 = (String) id1.get("videoId");
            String videoId2 = (String) id2.get("videoId");
            String videoId3 = "";

            videoIds.add(videoId1);
            videoIds.add(videoId2);
            videoIds.add(videoId3);

        } else if (dataCnt == 3) {
            JSONObject info1 = (JSONObject) items.get(0);
            JSONObject id1 = (JSONObject) info1.get("id");
            JSONObject info2 = (JSONObject) items.get(1);
            JSONObject id2 = (JSONObject) info2.get("id");
            JSONObject info3 = (JSONObject) items.get(2);
            JSONObject id3 = (JSONObject) info3.get("id");

            String videoId1 = (String) id1.get("videoId");
            String videoId2 = (String) id2.get("videoId");
            String videoId3 = (String) id3.get("videoId");

            videoIds.add(videoId1);
            videoIds.add(videoId2);
            videoIds.add(videoId3);
        }

        return videoIds;
    }

    public String getImg(String title, String artist) throws IOException {

        if (title.contains("&")) {
            title = title.replace("&", "");
        }

        if (artist.contains("&")) {
            artist = artist.replace("&", "");
        }

        String url = "https://www.genie.co.kr/search/searchMain?query=" + artist + " " + title + "&pagesize=1";

        Document doc = Jsoup.connect(url).get();
        Elements td = doc.select("tr.list > td");
        Element img_info = td.get(2);
        Element img_src = img_info.selectFirst("img");
        String img = img_src.attr("src");

        return img;
    }

    public List<String> getDetail(String title, String artist) throws IOException {

        List<String> details = new ArrayList<>();

        if (title.contains("&")) {
            title = title.replace("&", "");
        }

        if (artist.contains("&")) {
            artist = artist.replace("&", "");
        }

        String url = "https://www.genie.co.kr/search/searchMain?query=" + artist + " " + title + "&pagesize=1";
        Document d1 = Jsoup.connect(url).get();
        String songid = d1.selectFirst("tr.list").attr("songid");
        String song_url = "https://www.genie.co.kr/detail/songInfo?xgnm=" + songid;

        Document d2 = Jsoup.connect(song_url).get();
        Elements info = d2.select("ul.info-data > li");
        String genre = info.get(2).selectFirst("span.value").text();
        String duration = info.get(3).selectFirst("span.value").text();

        details.add(genre);
        details.add(duration);

        return details;
    }
}
