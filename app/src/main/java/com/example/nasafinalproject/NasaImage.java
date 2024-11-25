package com.example.nasafinalproject;

public class NasaImage {

    private long id;
    private String date;
    private String explanation;
    private String hdUrl;
    private String url;
    private String title;

    public NasaImage(long id, String title, String date, String explanation, String url, String hdUrl ) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.explanation = explanation;
        this.url = url;
        this.hdUrl = hdUrl;
    }

    public long getId() {
        return this.id;
    }

    public String getDate() {
        return this.date;
    }

    public String getExplanation() {
        return this.explanation;
    }

    public String getHdUrl() {
        return this.hdUrl;
    }

    public String getUrl() {
        return this.url;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setHdUrl(String hdUrl) {
        this.hdUrl = hdUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
