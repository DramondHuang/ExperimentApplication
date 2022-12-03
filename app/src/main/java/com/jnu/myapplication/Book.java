package com.jnu.myapplication;

import java.io.Serializable;

public class Book implements Serializable {
    int cover_id;
    String title;

    public Book(String t,int id){
        title=t;
        cover_id=id;
    }
    public int getCoverResource(){
        return cover_id;
    }
    public String getTitle(){
        return title;
    }
}

