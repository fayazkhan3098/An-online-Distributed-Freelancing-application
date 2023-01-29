package com.example.m_ployer.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class PostImageModel {
    private String imageURL, id, description, uid ;

    @ServerTimestamp
    private Date timestamp;

    public PostImageModel() {
    }

    public PostImageModel(String imageURL, String id, String description,String uid, Date timestamp) {
        this.imageURL = imageURL;
        this.id = id;
        this.uid = uid;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiscription() {
        return description;
    }

    public void setDiscription(String discription) {
        this.description = discription;
    }

    public String getUid() {    return uid;    }

    public void setUid(String uid) {  this.uid = uid;    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
