package sg.edu.nus.iss.Mini.Project.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.Mini.Project.model.Concert;
import sg.edu.nus.iss.Mini.Project.repo.MapRepo;
import sg.edu.nus.iss.Mini.Project.util.Utility;

@Service
public class ConcertRestService {
    
    @Autowired
    MapRepo mapRepo;

    RestTemplate template = new RestTemplate();
    
    public List<Concert> getAllConcert() {
        
        List<Concert> concert = new ArrayList<>();
        String concertData = template.getForObject(Utility.concertUrl, String.class);

        JsonReader reader = Json.createReader(new StringReader(concertData));
        JsonObject jObject = reader.readObject();

        JsonObject jObjectResult = jObject.getJsonObject("_embedded");
        JsonArray jArray = jObjectResult.getJsonArray("events");

        for(int i = 0; i < jArray.size(); i++) {
            JsonObject jObjectRecord = jArray.getJsonObject(i);

            Concert concerts = new Concert();
            concerts.setEventName(jObjectRecord.getString("name"));
            
            concerts.setImageUrl(jObjectRecord.getString("images"));
            concerts.setVenueName(jObjectRecord.getString("venues name"));
            concerts.setVenueUrl(jObjectRecord.getString("venues url"));
            concerts.setAttractionName(jObjectRecord.getString("attraction name"));
            concerts.setTicketPrice(jObject.getInt("ticketing"));

            concert.add(concerts);
        }
        
        return concert;
    }
    
}
