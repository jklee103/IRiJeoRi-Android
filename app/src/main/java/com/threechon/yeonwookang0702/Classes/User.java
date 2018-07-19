package com.threechon.yeonwookang0702.Classes;

public class User {
    public String username;
    public String email;

    public User() {
        // 가져올때 DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
