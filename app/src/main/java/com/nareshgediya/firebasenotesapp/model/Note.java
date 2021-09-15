package com.nareshgediya.firebasenotesapp.model;

public class Note {
    private String title;
    private String content;
    private String id;
    private String timeStamp;

    public Note(String id) {
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Note(){}
    public Note(String title,String content, String id, String timeStamp){
        this.title = title;
        this.content = content;
        this.id = id;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}