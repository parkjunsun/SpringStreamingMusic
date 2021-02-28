package js.StreamingMusic.service.data;

import com.google.common.net.UrlEscapers;
import js.StreamingMusic.exception.NotExistSearchResult;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
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
import java.util.HashMap;
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

        if (title.length() + artist.length() > 80) {
            String url = "https://www.genie.co.kr/search/searchMain?query=" + title + "&pagesize=1";
            Document doc = Jsoup.connect(url).get();
            Elements td = doc.select("tr.list > td");
            Element img_info = td.get(2);
            Element img_src = img_info.selectFirst("img");
            String img = img_src.attr("src");

            return img;

        } else {
            String url = "https://www.genie.co.kr/search/searchMain?query=" + artist + " " + title + "&pagesize=1";

            Document doc = Jsoup.connect(url).get();
            Elements td = doc.select("tr.list > td");
            Element img_info = td.get(2);
            Element img_src = img_info.selectFirst("img");
            String img = img_src.attr("src");

            return img;
        }

    }


    public String getImgByArtist(String artist) throws IOException {
        if (artist.contains("&")) {
            artist = artist.replace("&", "");
        }

        String url = "https://www.genie.co.kr/search/searchMain?query=" + artist;
        Document doc = Jsoup.connect(url).get();
        String src = doc.selectFirst("div.photo-zone > span.cover-img > a > img").attr("src");
        String img = "https:" + src;

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
        details.add(songid);


        return details;
    }

    public List<HashMap<String, String>> parsingAndRelocate(String jsonStr) throws ParseException {
        List<HashMap<String, String>> relocationSongs = new ArrayList<>();

        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(jsonStr);
        for (int i = 0; i < jsonArray.size(); i++) {
            HashMap<String, String> map = new HashMap<>();

            map.put("id", ((JSONObject)jsonArray.get(i)).get("id").toString());
            map.put("title", ((JSONObject)jsonArray.get(i)).get("title").toString());
            map.put("artist", ((JSONObject)jsonArray.get(i)).get("artist").toString());
            map.put("videoId", ((JSONObject)jsonArray.get(i)).get("videoId").toString());
            map.put("videoId2", ((JSONObject)jsonArray.get(i)).get("videoId2").toString());
            map.put("videoId3", ((JSONObject)jsonArray.get(i)).get("videoId3").toString());
            map.put("img", ((JSONObject)jsonArray.get(i)).get("img").toString());
            map.put("genre", ((JSONObject)jsonArray.get(i)).get("genre").toString());
            map.put("duration", ((JSONObject)jsonArray.get(i)).get("duration").toString());

            relocationSongs.add(map);
        }

        return relocationSongs;
    }


    public String refactoringName(String genre) {

        String queryColumn = "";

        if (genre.equals("발라드")) {
            String column1 = "가요 / 발라드;";
            String column2 = "가요 / 블루스/포크;";
            String column3 = "가요 / R&B/소울";
            queryColumn = column1 + column2 + column3;
        }
        else if (genre.equals("댄스")) { queryColumn = "가요 / 댄스"; }
        else if (genre.equals("락")) { queryColumn = "가요 / 락";}
        else if (genre.equals("힙합")) { queryColumn = "가요 / 랩/힙합"; }
        else if (genre.equals("일렉트로니카")) {queryColumn = "가요 / 일렉트로니카";}
        else if (genre.equals("인디뮤직")) {queryColumn = "가요 / 인디";}
        else if (genre.equals("트로트")) {queryColumn = "가요 / 트로트";}
        else if (genre.equals("유튜브")) {queryColumn = "youtube";}
        else if (genre.equals("기타")) {queryColumn = "기타";}

        return queryColumn;
    }


    public HashMap<String, String> getVideoInfo(String videoId) throws IOException, ParseException {

        HashMap<String, String> data = new HashMap<>();

        String urlStr = String.format("https://www.googleapis.com/youtube/v3/videos?id=%s&key=%s&part=contentDetails&part=snippet", videoId, api_key);
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

        JSONObject pageInfo = (JSONObject) obj.get("pageInfo");
        Long totalResults = (Long) pageInfo.get("totalResults");

        if (totalResults == 0) {
            throw new NotExistSearchResult("검색 결과가 없습니다");
        }


        JSONArray items = (JSONArray) obj.get("items");
        JSONObject info = (JSONObject) items.get(0);

        JSONObject snippet = (JSONObject) info.get("snippet");
        JSONObject contentDetails = (JSONObject) info.get("contentDetails");

        JSONObject thumbnails = (JSONObject) snippet.get("thumbnails");
        JSONObject high = (JSONObject) thumbnails.get("high");

        String img = (String) high.get("url");
        String title = (String) snippet.get("title");
        String time = (String) contentDetails.get("duration");

        PeriodFormatter formatter = ISOPeriodFormat.standard();
        String hours = Integer.toString(formatter.parsePeriod(time).getHours());
        String minutes = Integer.toString(formatter.parsePeriod(time).getMinutes());
        String seconds = Integer.toString(formatter.parsePeriod(time).getSeconds());
        String duration = "";

        if (hours.equals("0")) {
            if (minutes.length() == 1) {
                minutes = "0" + minutes;
            }

            if (seconds.length() == 1) {
                seconds = "0" + seconds;
            }

            duration = String.format("%s:%s", minutes, seconds);
        } else {

            if (minutes.length() == 1) {
                minutes = "0" + minutes;
            }

            if (seconds.length() == 1) {
                seconds = "0" + seconds;
            }

            duration = String.format("%s:%s:%s", hours, minutes, seconds);
        }


        data.put("title", title);
        data.put("img", img);
        data.put("duration", duration);

        return data;
    }
}
