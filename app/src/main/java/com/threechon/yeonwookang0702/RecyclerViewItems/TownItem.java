package com.threechon.yeonwookang0702.RecyclerViewItems;// 메인 페이지 리사이클러뷰 아이템
// 나중에 생성자 매개변수 변경시키기 (Database)

public class TownItem {
    private String key;
    private int townImage;
    private String townName;
    private String townAddress;

    // 키값으로 생성 (나중에 키 값 이용해서 가져와야함)
    public TownItem (String key){
        this.key = key;
    }



    // 임시 생성자 (Layout 샘플용)
    public TownItem (int townImage, String townName, String townAddress){
        this.townImage = townImage;
        this.townName = townName;
        this.townAddress = townAddress;
    }

    // 키 값은 DB로부터 가져오거나...
    // 키 값으로 API로부터 마을 정보 가지고 오는 메소드
    private void getTownInfo(String key) {

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
}
