package io.pivotal.hacker.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;


@Controller
public class NewsController {

    String[] top10urls = new String[10];
    String[] top10titles = new String[10];

    @Autowired
    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public ModelAndView showNews() {
        String sU = "this is a url";
        String sT = "this is a title";

        setTitlesAndIds();

        NewsInfo news = new NewsInfo(top10urls[0], top10titles[0]);
        ModelAndView modelAndView = new ModelAndView("show-news");
        modelAndView.getModel().put("newsInfo", news);

        NewsInfo news1 = new NewsInfo(top10urls[1], top10titles[1]);
        //ModelAndView modelAndView = new ModelAndView("show-news");
        modelAndView.getModel().put("newsInfo1", news1);

        return modelAndView;
    }


    public void setTitlesAndIds() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String top500IDs = restTemplate.exchange("https://hacker-news.firebaseio.com/v0/topstories.json", HttpMethod.GET, entity, String.class).getBody();

        String[] top500IdSplit = top500IDs.split(",");
        String idString;
        int count = 0;
        for (String topIdSplit : top500IdSplit) {
            if (count == 0) {
                topIdSplit = topIdSplit.substring(1, topIdSplit.length()); //first JSON string has "["; need to remove it
                idString = restTemplate.exchange("https://hacker-news.firebaseio.com/v0/item/" + topIdSplit + ".json", HttpMethod.GET, entity, String.class).getBody();
                displayTitle(idString, count);
                displayLink(idString, count);
            } else if (count < 10) {
                idString = restTemplate.exchange("https://hacker-news.firebaseio.com/v0/item/" + topIdSplit + ".json", HttpMethod.GET, entity, String.class).getBody();
                displayTitle(idString, count);
                displayLink(idString, count);
            } else {
                break;
            }
            count++;
        }
    }

    public void displayTitle(String jsonData, int count) {
        String jsonDataParsed, title;

        int titleIndex1 = jsonData.indexOf("title");
        if (titleIndex1 > -1) {
            jsonDataParsed = jsonData.substring(titleIndex1);
            int titleIndex2 = jsonDataParsed.indexOf(",");
            title = jsonDataParsed.substring(8, titleIndex2 - 1);
            top10titles[count] = title;
            System.out.println(title);
        }
    }

    public void displayLink(String jsonData, int count) {
        String jsonDataParsed, url;

        int urlIndex1 = jsonData.indexOf("url");
        if (urlIndex1 > -1) {
            jsonDataParsed = jsonData.substring(urlIndex1);
            int urlIndex2 = jsonDataParsed.indexOf("}");
            url = jsonDataParsed.substring(6, urlIndex2 - 1);
            top10urls[count] = url;
            System.out.println(url);
        }
    }

//    public void displayTitleAndLink(JSONArray jsonData){
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode rootNode = null;
//        try {
//            rootNode = objectMapper.readTree(jsonData.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        JsonNode titleNode = rootNode.path("title");
//        Iterator<JsonNode> elementsTitle = titleNode.elements();
//        while(elementsTitle.hasNext()) {
//            JsonNode title = elementsTitle.next();
//            System.out.println("title = " + title);
//        }
//        JsonNode urlNode = rootNode.path("url");
//        Iterator<JsonNode> elementsUrl = urlNode.elements();
//        while(elementsUrl.hasNext()) {
//            JsonNode url = elementsUrl.next();
//            System.out.println("url = " + url);
//        }
//    }


}
