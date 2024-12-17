package sg.edu.nus.iss.Mini.Project.model;

import java.util.Date;

public class Concert {
    
    // get event details api
    
    private String eventName;
    private String type;
    private String venue;
    private String url;
    private Date date;
    private Integer price;
    
    public Concert(String eventName, String type, String venue, String url, Date date, Integer price) {
        this.eventName = eventName;
        this.type = type;
        this.venue = venue;
        this.url = url;
        this.date = date;
        this.price = price;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return eventName + "," + type + "," + venue + "," + url + "," + date + "," + price;
    }
    
}
