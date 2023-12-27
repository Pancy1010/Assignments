package net.hbusstu.wastesorting.SearchPart;

import com.google.gson.Gson;

public class Utility {
    public static GarbageSearch handleGarbageSearchResponse(String response){
        try{
            return new Gson().fromJson(response, GarbageSearch.class);

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
