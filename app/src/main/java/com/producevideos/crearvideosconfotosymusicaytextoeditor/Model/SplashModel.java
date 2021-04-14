package com.producevideos.crearvideosconfotosymusicaytextoeditor.Model;

public class SplashModel {
    private String AppLink;
    private String AppName;
    private String AppUrl;

    public SplashModel(String AppUrl, String AppName, String AppLink) {
        this.AppName = AppName;
        this.AppLink = AppLink;
        this.AppUrl = AppUrl;
    }

    public String getAppName() {
        return this.AppName;
    }

    public void setAppName(String AppName) {
        this.AppName = AppName;
    }

    public String getAppLink() {
        return this.AppLink;
    }

    public void setAppLink(String AppIcon) {
        this.AppLink = AppIcon;
    }

    public String getAppUrl() {
        return this.AppUrl;
    }

    public void setAppUrl(String AppUrl) {
        this.AppUrl = AppUrl;
    }
}
