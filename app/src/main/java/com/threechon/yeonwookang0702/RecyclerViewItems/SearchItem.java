package com.threechon.yeonwookang0702.RecyclerViewItems;

public class SearchItem {
    private String key;
    private int townImage;
    private String townName;
    private String townAddress;
    private String townFunctions;
    private String townTag;

    private String imageUrl;
    private String townIntro;

    private String townId;
    private String townManager;
    private String townEmail;
    private String townHomepage;

    public SearchItem() {
    }

    public SearchItem(String townName, String townAddress, String townFunctions, String townTag, String imageUrl, String townIntro, String townId, String townManager, String townEmail, String townHomepage) {
        this.townName = townName;
        this.townAddress = townAddress;
        this.townFunctions = townFunctions;
        this.townTag = townTag;
        this.imageUrl = imageUrl;
        this.townIntro = townIntro;
        this.townId = townId;
        this.townManager = townManager;
        this.townEmail = townEmail;
        this.townHomepage = townHomepage;
    }

    public String getTownId() {
        return townId;
    }

    public void setTownId(String townId) {
        this.townId = townId;
    }

    public String getTownManager() {
        return townManager;
    }

    public void setTownManager(String townManager) {
        this.townManager = townManager;
    }

    public String getTownEmail() {
        return townEmail;
    }

    public void setTownEmail(String townEmail) {
        this.townEmail = townEmail;
    }

    public String getTownHomepage() {
        return townHomepage;
    }

    public void setTownHomepage(String townHomepage) {
        this.townHomepage = townHomepage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTownIntro() {
        return townIntro;
    }

    public void setTownIntro(String townIntro) {
        this.townIntro = townIntro;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getTownImage() {
        return townImage;
    }

    public void setTownImage(int townImage) {
        this.townImage = townImage;
    }

    public void setTownImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTownImage2() {
        return this.imageUrl;
    }


    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getTownAddress() {
        return townAddress;
    }

    public void setTownAddress(String townAddress) {
        this.townAddress = townAddress;
    }

    public String getTownFunctions() {
        return townFunctions;
    }

    public void setTownFunctions(String townFunctions) {
        this.townFunctions = townFunctions;
    }

    public String getTownTag() {
        return townTag;
    }

    public void setTownTag(String townTag) {
        this.townTag = townTag;
    }
}
