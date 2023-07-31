package com.sakura.types;

import java.util.ArrayList;

public class AnimeItemDetail {
    public String image;
    public String title;
    public String description;
    public String status;
    public boolean availability=true;
    public static class Columns{
        public String tip;
        public String url;
        public Columns(String tip,String url){
            this.tip=tip;
            this.url=url;
        }
    }
    public ArrayList<Columns> columns;
}
