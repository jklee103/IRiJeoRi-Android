package com.threechon.yeonwookang0702.RecyclerViewItems;

//내가 작성한 리뷰
public class MyReviewItem {
    String villageId;
    String villageName;
    String imageUrl;
    String reviewContent; // 리뷰 내용
    double reviewRating; // 리뷰 별점

    int review_index;

    String villageIntro;
    String villageAddress;
    String villageManager;
    String villageEmail;
    String villageHomepage;

    String useremail;
    String date;

    public MyReviewItem(String villageId, String villageName, String imageUrl, String reviewContent, double reviewRating, String villageIntro, String villageAddress, String villageManager, String villageEmail, String villageHomepage) {
        this.villageId = villageId;
        this.villageName = villageName;
        this.imageUrl = imageUrl;
        this.reviewContent = reviewContent;
        this.reviewRating = reviewRating;
        this.villageIntro = villageIntro;
        this.villageAddress = villageAddress;
        this.villageManager = villageManager;
        this.villageEmail = villageEmail;
        this.villageHomepage = villageHomepage;
    }

    public MyReviewItem(String villageId, String villageName, String imageUrl, String reviewContent, double reviewRating, String useremail, String date) {
        this.villageId = villageId;
        this.villageName = villageName;
        this.imageUrl = imageUrl;
        this.reviewContent = reviewContent;
        this.reviewRating = reviewRating;
        this.useremail = useremail;
        this.date = date;
    }

    public MyReviewItem(String villageId, String reviewContent, double reviewRating, String useremail, String date) {
        this.villageId = villageId;
        this.reviewContent = reviewContent;
        this.reviewRating = reviewRating;
        this.useremail = useremail;
        this.date = date;
    }

    public int getReview_index() {
        return review_index;
    }

    public void setReview_index(int review_index) {
        this.review_index = review_index;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public double getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(double reviewRating) {
        this.reviewRating = reviewRating;
    }

    public String getVillageIntro() {
        return villageIntro;
    }

    public void setVillageIntro(String villageIntro) {
        this.villageIntro = villageIntro;
    }

    public String getVillageAddress() {
        return villageAddress;
    }

    public void setVillageAddress(String villageAddress) {
        this.villageAddress = villageAddress;
    }

    public String getVillageManager() {
        return villageManager;
    }

    public void setVillageManager(String villageManager) {
        this.villageManager = villageManager;
    }

    public String getVillageEmail() {
        return villageEmail;
    }

    public void setVillageEmail(String villageEmail) {
        this.villageEmail = villageEmail;
    }

    public String getVillageHomepage() {
        return villageHomepage;
    }

    public void setVillageHomepage(String villageHomepage) {
        this.villageHomepage = villageHomepage;
    }
}
