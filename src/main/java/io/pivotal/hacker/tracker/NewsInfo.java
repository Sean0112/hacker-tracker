package io.pivotal.hacker.tracker;

public class NewsInfo {

    private String url;
    private String title;

    public NewsInfo(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }
}
