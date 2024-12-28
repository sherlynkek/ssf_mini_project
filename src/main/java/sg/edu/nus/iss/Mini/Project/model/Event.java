package sg.edu.nus.iss.Mini.Project.model;

import java.time.LocalDate;


public class Event {
    
    // get all event details api
    private String id;
    private String eventName;
    private LocalDate date;
    private String imageUrl;
    private String venueName;
    private String venueUrl;
    private String attractionName;
    private String classificationName;
    private Double ticketPriceLow;
    private Double ticketPriceHigh;
    private String ticketUrl;   
 
    public Event(String id, String eventName, LocalDate date, String imageUrl, String venueName, String venueUrl,
            String attractionName, String classificationName, Double ticketPriceLow, Double ticketPriceHigh,
            String ticketUrl) {
        this.id = id;
        this.eventName = eventName;
        this.date = date;
        this.imageUrl = imageUrl;
        this.venueName = venueName;
        this.venueUrl = venueUrl;
        this.attractionName = attractionName;
        this.classificationName = classificationName;
        this.ticketPriceLow = ticketPriceLow;
        this.ticketPriceHigh = ticketPriceHigh;
        this.ticketUrl = ticketUrl;
    }

    public Event() {
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    @Override
    public String toString() {
        return eventName + "," + date + "," + imageUrl + ","
                + venueName + "," + venueUrl + "," + attractionName + "," + classificationName
                + "," + ticketPriceLow + "," + ticketPriceHigh + "," + ticketUrl;
    }

}
