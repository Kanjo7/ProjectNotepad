package com.projectnotepad.projectnotepad;

import javax.persistence.*;

@Entity
@Table(name = "tag")

public class Tag {

    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tagId;

    @Column(name = "tag_content")
    private String tagContent;

    // konstruktor
    public Tag() {
    }

    public Tag(int tagId, String tagContent) {
        this.tagId = tagId;
        this.tagContent = tagContent;
    }

    public Tag(String tagContent) {
        this.tagContent = tagContent;
    }

    // Getters och Setters
    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagContent() {
        return tagContent;
    }

    public void setTagContent(String tagContent) {
        this.tagContent = tagContent;
    }
}
