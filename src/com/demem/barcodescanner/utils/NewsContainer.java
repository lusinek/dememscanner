package com.demem.barcodescanner.utils;

public class NewsContainer {
    private String newsContent;
    private String imagePath;

    public NewsContainer(){}

    public NewsContainer(String newsContent, String imagePath){
        this.newsContent = newsContent;
        this.imagePath = imagePath;
    }

    public String getNewsContent() {
        return newsContent;
    }
    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
