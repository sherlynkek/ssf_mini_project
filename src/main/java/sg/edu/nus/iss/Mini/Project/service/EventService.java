package sg.edu.nus.iss.Mini.Project.service;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.Mini.Project.model.Event;
import sg.edu.nus.iss.Mini.Project.util.Utility;

@Service
public class EventService {
    @Value("${spring.data.api}")
    private String apiKey;

    RestTemplate template = new RestTemplate();
    
    public List<Event> getAllEvent() {
        
        List<Event> event = new ArrayList<>();
        // System.out.println(Utility.eventUrl);

        String url = Utility.eventUrl + apiKey;
        // System.out.println(url); 

        String eventData = template.getForObject(url, String.class);

        JsonReader reader = Json.createReader(new StringReader(eventData));
        JsonObject jObject = reader.readObject();

        JsonObject jObjectResult = jObject.getJsonObject("_embedded");
        JsonArray jArray = jObjectResult.getJsonArray("events");

        for(int i = 0; i < jArray.size(); i++) {
            JsonObject jObjectRecord = jArray.getJsonObject(i);

           // image
            JsonArray jArrayImage = jObjectRecord.getJsonArray("images");
            JsonObject jObjectImage = jArrayImage.getJsonObject(0);
            String imageURL = jObjectImage.getString("url");

            // date
            JsonObject jObjectDate = jObjectRecord.getJsonObject("dates");
            JsonObject jObjectDates = jObjectDate.getJsonObject("start");
            String localDate = jObjectDates.getString("localDate");
        
            // classification
            JsonArray jArrayClass = jObjectRecord.getJsonArray("classifications");
            JsonObject jObjectClass = jArrayClass.getJsonObject(0);
            JsonObject jObjectClassified = jObjectClass.getJsonObject("segment");
            String classType = jObjectClassified.getString("name");

            // ticketing price range (low and high)
            JsonArray jArrayTix = jObjectRecord.getJsonArray("priceRanges");
            // System.out.println(i);
            // System.out.println(jArrayTix == null);
            // System.out.println(jObjectRecord.containsKey("priceRanges"));
            // JsonObject jObjectTix = jArrayTix.getJsonObject(0);
            Double priceLow = null;
            Double priceHigh = null;
            if(jArrayTix != null && !jArrayTix.isEmpty()) {
                JsonObject jObjectTix = jArrayTix.getJsonObject(0);
                priceLow = jObjectTix.getJsonNumber("min").doubleValue();
                priceHigh = jObjectTix.getJsonNumber("max").doubleValue();
            }

            // venue 
            JsonObject jsonObjectVenue = jObjectRecord.getJsonObject("_embedded");
            String venueName = null;
            String venueURL = null;
            if(jsonObjectVenue != null) {
                JsonArray jArrayVenue = jsonObjectVenue.getJsonArray("venues");
                if(jArrayVenue != null) {
                    JsonObject jObjectVenue = jArrayVenue.getJsonObject(0);
                    venueName = jObjectVenue.getString("name");
                    venueURL = jObjectVenue.getString("url", "No url provided");
                }
            }
                              
            // attraction
            JsonArray jArrayAttract = jsonObjectVenue.getJsonArray("attractions");
            JsonObject jObjectAttract = jArrayAttract.getJsonObject(0);
            String attractName = jObjectAttract.getString("name");

            Event events = new Event();
            events.setId(jObjectRecord.getString("id"));
            events.setEventName(jObjectRecord.getString("name"));
            events.setTicketUrl(jObjectRecord.getString("url"));
            events.setDate(LocalDate.parse(localDate));
            events.setImageUrl(imageURL);
            events.setVenueName(venueName);
            events.setVenueUrl(venueURL);
            events.setAttractionName(attractName);
            events.setTicketPriceLow(priceLow);
            events.setTicketPriceHigh(priceHigh);
            events.setClassificationName(classType);

            event.add(events);
        } 
        return event;
    }
    
    public Event getEventFromId(String id) {
        String url = UriComponentsBuilder
        .fromUriString(Utility.eventIdUrl)
        .path("/{id}")
        .queryParam("apikey", apiKey)
        .build(id)
        .toString();
        // System.out.println(url);
        
        ResponseEntity<String> res = template.getForEntity(url, String.class);
        JsonReader reader = Json.createReader(new StringReader(res.getBody()));
        JsonObject jObject = reader.readObject();
        
        // image
        JsonArray jArrayImage = jObject.getJsonArray("images");
        JsonObject jObjectImage = jArrayImage.getJsonObject(0);
        String imageURL = jObjectImage.getString("url");

        // date
        JsonObject jObjectDate = jObject.getJsonObject("dates");
        JsonObject jObjectDates = jObjectDate.getJsonObject("start");
        String localDate = jObjectDates.getString("localDate");
        
        // classification
        JsonArray jArrayClass = jObject.getJsonArray("classifications");
        JsonObject jObjectClass = jArrayClass.getJsonObject(0);
        JsonObject jObjectClassified = jObjectClass.getJsonObject("segment");
        String classType = jObjectClassified.getString("name");

        // ticketing price range (low and high)
        JsonArray jArrayTix = jObject.getJsonArray("priceRanges");
        Double priceLow = null;
        Double priceHigh = null;
        if(jArrayTix != null && !jArrayTix.isEmpty()) {
            JsonObject jObjectTix = jArrayTix.getJsonObject(0);
            priceLow = jObjectTix.getJsonNumber("min").doubleValue();
            priceHigh = jObjectTix.getJsonNumber("max").doubleValue();
        }

        // venue 
        JsonObject jsonObjectVenue = jObject.getJsonObject("_embedded");
        String venueName = null;
        String venueURL = null;
        if(jsonObjectVenue != null) {
            JsonArray jArrayVenue = jsonObjectVenue.getJsonArray("venues");
            if(jArrayVenue != null) {
                JsonObject jObjectVenue = jArrayVenue.getJsonObject(0);
                venueName = jObjectVenue.getString("name");
                venueURL = jObjectVenue.getString("url", "No url provided");
            }
        }
                              
        // attraction
        JsonArray jArrayAttract = jsonObjectVenue.getJsonArray("attractions");
        JsonObject jObjectAttract = jArrayAttract.getJsonObject(0);
        String attractName = jObjectAttract.getString("name");

        Event events = new Event();
        events.setId(jObject.getString("id"));
        events.setEventName(jObject.getString("name"));
        events.setTicketUrl(jObject.getString("url"));
        events.setDate(LocalDate.parse(localDate));
        events.setImageUrl(imageURL);
        events.setVenueName(venueName);
        events.setVenueUrl(venueURL);
        events.setAttractionName(attractName);
        events.setTicketPriceLow(priceLow);
        events.setTicketPriceHigh(priceHigh);
        events.setClassificationName(classType);

        return events;
    }

    public List<Event> getEventsByIds(List<String> eventIds) {
        List<Event> events = new ArrayList<>();

        for (String eventId : eventIds) {
            try {
                Event event = getEventFromId(eventId);
                if (event != null) {
                    events.add(event);
                }
            } catch (Exception e) {
                // Log and skip any errors for invalid IDs
                System.err.println("Error fetching event with ID: " + eventId);
                e.printStackTrace();
            }
        }

        return events;
    }
}
