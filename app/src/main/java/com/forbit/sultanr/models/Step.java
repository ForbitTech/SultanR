package com.forbit.sultanr.models;

public class Step {

    private String title;
    private String content;

    public Step() {
    }

    public Step(String title, String content) {
        this.title = title;
        this.content = content;
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
