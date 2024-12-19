package sg.edu.nus.iss.Mini.Project.service;

import java.io.StringReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.Mini.Project.model.Event;
import sg.edu.nus.iss.Mini.Project.repo.MapRepo;
import sg.edu.nus.iss.Mini.Project.util.Utility;

@Service
public class EventRestService {
    @Value("${spring.data.api}")
    private String apiKey;

    @Autowired
    MapRepo mapRepo;

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

            // image array and object
            
            JsonArray jArrayImage = jObjectRecord.getJsonArray("images");
            JsonObject jObjectImage = jArrayImage.getJsonObject(0);
            String imageURL = jObjectImage.getString("url"); 

            // date array and object
            JsonObject jObjectDate = jObjectRecord.getJsonObject("dates");
            JsonObject jObjectDates = jObjectDate.getJsonObject("start");
            String localDate = jObjectDates.getString("localDate");

            // venue array and object
            JsonObject jsonObjectVenue = jObjectRecord.getJsonObject("_embedded");
            JsonArray jArrayVenue = jsonObjectVenue.getJsonArray("venues");
            JsonObject jObjectVenue = jArrayVenue.getJsonObject(0);
            String venueName = jObjectVenue.getString("name");
            String venueURL = jObjectVenue.getString("url");

            // attraction array
            JsonArray jArrayAttract = jsonObjectVenue.getJsonArray("attractions");
            JsonObject jObjectAttract = jArrayAttract.getJsonObject(0);
            String attractName = jObjectAttract.getString("name");

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

            Event events = new Event();
            events.setEventName(jObjectRecord.getString("name"));
            events.setTicketUrl(jObjectRecord.getString("url"));

            events.setDate(Date.valueOf(localDate));

            events.setImageUrl(imageURL);

            events.setVenueName(venueName);
            events.setVenueUrl(venueURL);

            events.setAttractionName(attractName);
            events.setTicketPriceLow(priceLow);
            events.setTicketPriceHigh(priceHigh);

            event.add(events);
        }
        
        return event;
    }
    
}
