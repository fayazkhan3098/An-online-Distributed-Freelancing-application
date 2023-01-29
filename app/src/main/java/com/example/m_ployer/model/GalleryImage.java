package com.example.m_ployer.model;

import android.net.Uri;

public class GalleryImage {
    public Uri picUri;


    public GalleryImage() {
    }

    public GalleryImage(Uri picUri) {
        this.picUri = picUri;

    }

    public Uri getPicUri() {
        return picUri;
    }

    public void setPicUri(Uri picUri) {
        this.picUri = picUri;
    }


}