package io.pivotal.hacker.tracker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Arrays;


@Controller
public class NewsController {

    String[] top10urls = new String[10];
    String[] top10titles = new String[10];

    @Autowired
    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public ModelAndView showNews() {
        setTitlesAndIds();

        NewsInfo news = new NewsInfo(top10urls[0], top10titles[0]);
        ModelAndView modelAndView = new ModelAndView("show-news");
        modelAndView.getModel().put("newsInfo", news);

        NewsInfo news1 = new NewsInfo(top10urls[1], top10titles[1]);
        modelAndView.getModel().put("newsInfo1", news1);

        NewsInfo news2 = new NewsInfo(top10urls[2], top10titles[2]);
        modelAndView.getModel().put("newsInfo2", news2);

        NewsInfo news3 = new NewsInfo(top10urls[3], top10titles[3]);
        modelAndView.getModel().put("newsInfo3", news3);

        NewsInfo news4 = new NewsInfo(top10urls[4], top10titles[4]);
        modelAndView.getModel().put("newsInfo4", news4);

        NewsInfo news5 = new NewsInfo(top10urls[5], top10titles[5]);
        modelAndView.getModel().put("newsInfo5", news5);

        NewsInfo news6 = new NewsInfo(top10urls[6], top10titles[6]);
        modelAndView.getModel().put("newsInfo6", news6);

        NewsInfo news7 = new NewsInfo(top10urls[7], top10titles[7]);
        modelAndView.getModel().put("newsInfo7", news7);

        NewsInfo news8 = new NewsInfo(top10urls[8], top10titles[8]);
        modelAndView.getModel().put("newsInfo8", news8);

        NewsInfo news9 = new NewsInfo(top10urls[9], top10titles[9]);
        modelAndView.getModel().put("newsInfo9", news9);

        return modelAndView;
    }


//    public void setTitlesAndIds() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        HttpEntity<String> entity = new HttpEntity<String>(headers);
//        String top500IDs = restTemplate.exchange("https://hacker-news.firebaseio.com/v0/topstories.json", HttpMethod.GET, entity, String.class).getBody();
//
//        String[] top500IdSplit = top500IDs.split(",");
//        String idString;
//        int count = 0;
//        for (String topIdSplit : top500IdSplit) {
//            if (count == 0) {
//                topIdSplit = topIdSplit.substring(1, topIdSplit.length()); //first JSON string has "["; need to remove it
//                idString = restTemplate.exchange("https://hacker-news.firebaseio.com/v0/item/" + topIdSplit + ".json", HttpMethod.GET, entity, String.class).getBody();
//                displayTitle(idString, count);
//                displayLink(idString, count);
//            } else if (count < 10) {
//                idString = restTemplate.exchange("https://hacker-news.firebaseio.com/v0/item/" + topIdSplit + ".json", HttpMethod.GET, entity, String.class).getBody();
//                displayTitle(idString, count);
//                displayLink(idString, count);
//            } else {
//                break;
//            }
//            count++;
//        }
//    }
//
//    public void displayTitle(String jsonData, int count) {
//        String jsonDataParsed, title;
//
//        int titleIndex1 = jsonData.indexOf("title");
//        if (titleIndex1 > -1) {
//            jsonDataParsed = jsonData.substring(titleIndex1);
//            int titleIndex2 = jsonDataParsed.indexOf(",");
//            title = jsonDataParsed.substring(8, titleIndex2 - 1);
//            top10titles[count] = title;
//            System.out.println(title);
//        }
//    }
//
//    public void displayLink(String jsonData, int count) {
//        String jsonDataParsed, url;
//
//        int urlIndex1 = jsonData.indexOf("url");
//        if (urlIndex1 > -1) {
//            jsonDataParsed = jsonData.substring(urlIndex1);
//            int urlIndex2 = jsonDataParsed.indexOf("}");
//            url = jsonDataParsed.substring(6, urlIndex2 - 1);
//            top10urls[count] = url;
//            System.out.println(url);
//        }
//    }

    public void setTitlesAndIds() {
        JSONArray top10 = new JSONArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String id = restTemplate.exchange("https://hacker-news.firebaseio.com/v0/topstories.json", HttpMethod.GET, entity, String.class).getBody();
        String[] id10;// = new String[10];
        id10 = id.substring(1).split(",");
        for (int i=0; i<10; i++) {
            top10.put(restTemplate.exchange("https://hacker-news.firebaseio.com/v0/item/"+id10[i]+".json", HttpMethod.GET, entity, String.class).getBody());
        }
        try {
            titleAndUrl(top10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void titleAndUrl(JSONArray json) throws IOException {
        for (int i=0; i<10; i++) {
//create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
//read JSON like DOM Parser
            JsonNode rootNode = objectMapper.readTree(json.get(i).toString());
            JsonNode titleNode = rootNode.path("title");
            JsonNode urlNode = rootNode.path("url");
            top10titles[i] = titleNode.asText();
            top10urls[i] = urlNode.asText();
            System.out.println("TITLE<---- " + titleNode.asText());
            System.out.println("URL<---- " + urlNode.asText());
        }
    }

}
