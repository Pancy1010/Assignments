package net.hbusstu.wastesorting.SearchPart;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GarbageGSON {
    public class News{
        @SerializedName("name")
        public String Garbagename;
        public int type;
        public String explain;
        public String contain;
        public String tip;
    }
//    public class GarbageSearch{
//        public String code;
//        public String msg;
//        @SerializedName("newslist")
//        public List<News> Newslist;
//    }
}
