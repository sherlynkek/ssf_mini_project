package sg.edu.nus.iss.Mini.Project.model;

import java.util.Date;

public class Concert {
    
    // get event (classification = music) details api
    // image url keep 
    // seatmap to be considered after everything is done
    
    private String eventName;
    private Date date;
    private String imageUrl;
    private String venueName;
    private String venueUrl;
    private String attractionName;
    private Integer ticketPrice;
    // private String seatMap;
    
    public Concert(String eventName, Date date, String imageUrl, String venueName, String venueUrl,
            String attractionName, Integer ticketPrice) {
        this.eventName = eventName;
        this.date = date;
        this.imageUrl = imageUrl;
        this.venueName = venueName;
        this.venueUrl = venueUrl;
        this.attractionName = attractionName;
        this.ticketPrice = ticketPrice;
        // this.seatMap = seatMap;
    }

    public Concert() {
        //TODO Auto-generated constructor stub
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueUrl() {
        return venueUrl;
    }

    public void setVenueUrl(String venueUrl) {
        this.venueUrl = venueUrl;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public Integer getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Integer ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    // public String getSeatMap() {
    //     return seatMap;
    // }

    // public void setSeatMap(String seatMap) {
    //     this.seatMap = seatMap;
    // }

    @Override
    public String toString() {
        return eventName + "," + date + "," + imageUrl + "," + venueName
                + "," + venueUrl + "," + attractionName + ","
                + ticketPrice;
    }

    
    
    
}
