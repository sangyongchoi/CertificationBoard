package com.example.certificationboard.post.domain;

public class TextContents implements Contents {

    private String title;
    private String contents;

    public TextContents(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }


    @Override
    public String toString() {
        return "TextContents{" +
                "title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
