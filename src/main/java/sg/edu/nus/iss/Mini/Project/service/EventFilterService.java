package sg.edu.nus.iss.Mini.Project.service;

import java.io.StringReader;
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
import sg.edu.nus.iss.Mini.Project.util.Utility;


@Service
public class EventFilterService {
    
    @Value("${spring.data.api}")
    private String apiKey;

    @Autowired
    EventService eventService;
    
    RestTemplate template = new RestTemplate();

    public List<Event> getFilterEvent(String classification) {
        
        List<Event> event = new ArrayList<>();

        String url = Utility.eventUrl + apiKey;

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
        
            // classification
            JsonArray jArrayClass = jObjectRecord.getJsonArray("classifications");
            JsonObject jObjectClass = jArrayClass.getJsonObject(0);
            JsonObject jObjectClassified = jObjectClass.getJsonObject("segment");
            String classType = jObjectClassified.getString("name");

            // ticketing price range (low and high)
            JsonArray jArrayTix = jObjectRecord.getJsonArray("priceRanges");
            Double priceLow = null;
            Double priceHigh = null;
            if(jArrayTix != null && !jArrayTix.isEmpty()) {
                JsonObject jObjectTix = jArrayTix.getJsonObject(0);
                priceLow = jObjectTix.getJsonNumber("min").doubleValue();
                priceHigh = jObjectTix.getJsonNumber("max").doubleValue();
            }

            Event events = new Event();
 
            events.setEventName(jObjectRecord.getString("name"));
            events.setImageUrl(imageURL);
            events.setClassificationName(classType);
            events.setTicketPriceHigh(priceHigh);
            events.setTicketPriceLow(priceLow);

            // Filter by classification (classType)
            if (classification != null && !classification.isEmpty() && classType.equalsIgnoreCase(classification)) {
                event.add(events);
            }
        }

        return event;
    }
}



