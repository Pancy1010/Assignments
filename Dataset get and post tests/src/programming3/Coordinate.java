package programming3;

import java.math.BigDecimal;

/**
 * @author Zhang Runyan
 */

public class Coordinate {
    
    private String time;
    private String username;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String description;

    public Coordinate (String timeSent, BigDecimal latitude, BigDecimal longitude){

        this.time = timeSent;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public void setTime(String time){
        this.time = time;
    }

    public BigDecimal getLongitude(){
        return this.longitude;
    }

    public BigDecimal getLatitude(){
        return this.latitude;
    }

    public String getTime(){
        return this.time;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public void setDescription(String desc){
        this.description = desc;
    }

    public String getDescription(){
        return this.description;
    }


}
