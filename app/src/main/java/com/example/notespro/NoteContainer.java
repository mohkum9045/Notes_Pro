package com.example.notespro;

import com.google.firebase.Timestamp;

public class NoteContainer {
    String Title;
    String Content;
    Timestamp Timestamp;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Timestamp getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.Timestamp = timestamp;
    }

    public  NoteContainer() {
    }
}
