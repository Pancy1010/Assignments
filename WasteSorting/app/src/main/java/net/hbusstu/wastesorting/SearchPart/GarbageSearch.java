package net.hbusstu.wastesorting.SearchPart;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GarbageSearch {
    public String code;
    public String msg;
    @SerializedName("newslist")
    public List<GarbageGSON.News> Newslist;
}