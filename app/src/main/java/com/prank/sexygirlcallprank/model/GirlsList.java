package com.prank.sexygirlcallprank.model;

import java.io.Serializable;

public class GirlsList implements Serializable {

    private String id;
    private String contact;
    private String name;
    private int avatar;
    private int video;

    public GirlsList(String id, String contact, String name, int avatar, int video) {
        this.id = id;
        this.contact = contact;
        this.name = name;
        this.avatar = avatar;
        this.video = video;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }
}
