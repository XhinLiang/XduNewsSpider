package com.xdu.xhin.bean;

/**
 * Created by xhinliang on 15-10-4.
 * Bean
 */
public class XduNews {

    private int id;
    private String newsHref;
    private String newsTitle;
    private String newsSource;

    public XduNews(int id, String newsHref, String newsTitle, String newsSource) {
        this.id = id;
        this.newsHref = newsHref;
        this.newsTitle = newsTitle;
        this.newsSource = newsSource;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsHref() {

        return newsHref;
    }

    public int getId() {
        return id;
    }

    public String getFileName() {
        return id + "." + newsTitle;
    }

    @Override
    public String toString() {
        return id + ", " + newsHref + ", " + newsTitle + ", " + newsSource;
    }
}
