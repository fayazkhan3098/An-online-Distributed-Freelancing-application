package com.example.m_ployer.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class HomeModel {
    private String userName, profileImage, imageUrl, uid, description, id;
    @ServerTimestamp
    private Date timestamp;
    private List<String> Liked, Applied;


    public HomeModel() {
    }

    public HomeModel(String userName, String profileImage, String imageUrl, String uid
            , String description, String id, Date timestamp, List<String> liked
            , List<String> applied) {
        this.userName = userName;
        this.profileImage = profileImage;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.description = description;
        this.id = id;
        this.timestamp = timestamp;
        Liked = liked;
        Applied = applied;
    }

    public String getUserName() {
        return userName;
    }

    public List<String> getLiked() {
        return Liked;
    }

    public void setLiked(List<String> liked) {
        Liked = liked;
    }

    public List<String> getApplied() {
        return Applied;
    }

    public void setApplied(List<String> applied) {
        Applied = applied;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


}
