package io.pivotal.hacker.tracker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

@RestController
public class JsonController{

    String[] top10Ids = new String[10];

//    @GetMapping("/")
//    public String sayHello() {
//        return "hello";
//    }

    @Autowired
    RestTemplate restTemplate = new RestTemplate();


    @GetMapping("/")
    public String getTop500() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String top500IDs = restTemplate.exchange("https://hacker-news.firebaseio.com/v0/topstories.json", HttpMethod.GET, entity, String.class).getBody();

        String[] top500IdSplit = top500IDs.split(",");
        String id0 = "hello";
        int count = 0;
        for(String s : top500IdSplit){
            if(count == 0){
                String s0 = s.substring(1, s.length());
                top10Ids[count] = s0;

                id0 = restTemplate.exchange("https://hacker-news.firebaseio.com/v0/item/" + s0 + ".json", HttpMethod.GET, entity, String.class).getBody();
                displayTitleAndLink(id0);
                //System.out.println(s0);
            }
            else if(count < 10){
                //System.out.println(s);
                top10Ids[count] = s;
                //code goes here
                String id = restTemplate.exchange("https://hacker-news.firebaseio.com/v0/item/" + s + ".json", HttpMethod.GET, entity, String.class).getBody();
                displayTitleAndLink(id);
            }
            else{


                break;
            }
            count++;
        }
        return id0;
    }

    public void displayTitleAndLink(String jsonData){

        int titleIndex1, titleIndex2;
        String jsonDataParsed, title, url;

            titleIndex1 = jsonData.indexOf("title");
            if (titleIndex1 > -1){
                jsonDataParsed = jsonData.substring(titleIndex1);
                titleIndex2 = jsonDataParsed.indexOf(",");
                title = jsonDataParsed.substring(8, titleIndex2-1);
                System.out.println(title);
            }
            int urlIndex1 = jsonData.indexOf("url");
            int urlIndex2;
            if(urlIndex1 > -1){
                jsonDataParsed = jsonData.substring(urlIndex1);
                urlIndex2 = jsonDataParsed.indexOf("}");
                url = jsonDataParsed.substring(6, urlIndex2-1);
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
