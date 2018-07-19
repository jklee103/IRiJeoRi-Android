package com.threechon.yeonwookang0702.RecyclerViewItems;

public class ReviewItem {
    String reviewContent; // 리뷰 내용
    double reviewRating; // 리뷰 별점

    public ReviewItem(String reviewContent, double reviewRating) {
        this.reviewContent = reviewContent;
        this.reviewRating = reviewRating;
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
}
