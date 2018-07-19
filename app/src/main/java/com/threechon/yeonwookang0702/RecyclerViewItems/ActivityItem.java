package com.threechon.yeonwookang0702.RecyclerViewItems;// 메인 페이지 리사이클러뷰 아이템
// 나중에 생성자 매개변수 변경시키기 (Database)

public class ActivityItem {
    private String key;
    private int activityImage;

    private String activityName;
    private String activityTag;
    private String activityIntro;
    private String activityMin;
    private String activityMax;
    private String activityPrice;

    private String imageUrl;

    private String activityId;
    private String villageId;
    private String activityCode;

    public ActivityItem(String activityName, String activityTag, String activityIntro, String activityMin, String activityMax, String activityPrice, String imageUrl, String activityId, String villageId, String activityCode) {
        this.activityName = activityName;
        this.activityTag = activityTag;
        this.activityIntro = activityIntro;
        this.activityMin = activityMin;
        this.activityMax = activityMax;
        this.activityPrice = activityPrice;
        this.imageUrl = imageUrl;
        this.activityId = activityId;
        this.villageId = villageId;
        this.activityCode = activityCode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getActivityImage() {
        return activityImage;
    }

    public void setActivityImage(int activityImage) {
        this.activityImage = activityImage;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityTag() {
        return activityTag;
    }

    public void setActivityTag(String activityTag) {
        this.activityTag = activityTag;
    }

    public String getActivityIntro() {
        return activityIntro;
    }

    public void setActivityIntro(String activityIntro) {
        this.activityIntro = activityIntro;
    }

    public String getActivityMin() {
        return activityMin;
    }

    public void setActivityMin(String activityMin) {
        this.activityMin = activityMin;
    }

    public String getActivityMax() {
        return activityMax;
    }

    public void setActivityMax(String activityMax) {
        this.activityMax = activityMax;
    }

    public String getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(String activityPrice) {
        this.activityPrice = activityPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }
}
