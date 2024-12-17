package sg.edu.nus.iss.Mini.Project.model;

import java.util.Date;

public class Concert {
    
    // get event details api
    // image and seatmap optional
    
    private String eventName;
    private Date date;
    private String image;
    private String venueName;
    private String venueLocation;
    private String attractionName;
    private Integer ticketPrice;
    private String seatMap;
    
    public Concert(String eventName, Date date, String image, String venueName, String venueLocation,
            String attractionName, Integer ticketPrice, String seatMap) {
        this.eventName = eventName;
        this.date = date;
        this.image = image;
        this.venueName = venueName;
        this.venueLocation = venueLocation;
        this.attractionName = attractionName;
        this.ticketPrice = ticketPrice;
        this.seatMap = seatMap;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueLocation() {
        return venueLocation;
    }

    public void setVenueLocation(String venueLocation) {
        this.venueLocation = venueLocation;
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

    public String getSeatMap() {
        return seatMap;
    }

    public void setSeatMap(String seatMap) {
        this.seatMap = seatMap;
    }

    @Override
    public String toString() {
        return eventName + "," + date + "," + image + "," + venueName
                + "," + venueLocation + "," + attractionName + ","
                + ticketPrice + "," + seatMap;
    }

    
    
    
}
