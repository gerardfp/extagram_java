package com.example.productmanager;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID postid;

    public String content;
    public String photourl;

    public Post(){}

    public Post(String content, String photourl) {
        this.content = content;
        this.photourl = photourl;
    }
}
