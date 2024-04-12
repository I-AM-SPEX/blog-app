package com.example.blog.models;

import java.io.Serializable;

public class    ContentModel implements Serializable {
    String title,content,authorName,date,id,time,userId,image;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ContentModel(String title, String content, String authorName, String date, String time, String id, String userId, String image) {
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.date = date;
        this.time = time;
        this.id = id;
        this.userId = userId;
        this.image = image;
    }



    public ContentModel() {
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

  

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public  static String readTime(String post) {
        int wordPerMinute = 200;
        int noOfWords = post.split("\\s+").length;
        double minutes = (double) noOfWords / wordPerMinute;
        int roundedMinutes = (int) Math.ceil(minutes);

        String minuteOrMinutes = roundedMinutes == 1 ? "minute" : "minutes";
        return roundedMinutes + " " + minuteOrMinutes + " read";
    }
}
