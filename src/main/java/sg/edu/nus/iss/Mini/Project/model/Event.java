package sg.edu.nus.iss.Mini.Project.model;

import java.util.Date;

public class Event {
    
    // get all event details api
    // image url keep 
    // seatmap to be considered after everything is done
    
    private String eventName;
    private Date date;
    private String imageUrl;
    private String venueName;
    private String venueUrl;
    private String attractionName;
    private Double ticketPriceLow;
    private Double ticketPriceHigh;
    private String ticketUrl;
    // private String seatMap;
    
    public Event(String eventName, Date date, String imageUrl, String venueName, String venueUrl, String attractionName,
            Double ticketPriceLow, Double ticketPriceHigh, String ticketUrl) {
        this.eventName = eventName;
        this.date = date;
        this.imageUrl = imageUrl;
        this.venueName = venueName;
        this.venueUrl = venueUrl;
        this.attractionName = attractionName;
        this.ticketPriceLow = ticketPriceLow;
        this.ticketPriceHigh = ticketPriceHigh;
        this.ticketUrl = ticketUrl;
        // this.seatMap = seatMap;
    }

    public Event() {
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

    public Double getTicketPriceLow() {
        return ticketPriceLow;
    }

    public void setTicketPriceLow(Double ticketPriceLow) {
        this.ticketPriceLow = ticketPriceLow;
    }

    public Double getTicketPriceHigh() {
        return ticketPriceHigh;
    }

    public void setTicketPriceHigh(Double ticketPriceHigh) {
        this.ticketPriceHigh = ticketPriceHigh;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    // public String getSeatMap() {
    //     return seatMap;
    // }

    // public void setSeatMap(String seatMap) {
    //     this.seatMap = seatMap;
    // }

    @Override
    public String toString() {
        return eventName + "," + date + "," + imageUrl + ","
                + venueName + "," + venueUrl + "," + attractionName + ","
                + ticketPriceLow + "," + ticketPriceHigh + "," + ticketUrl;
    }

    
}
