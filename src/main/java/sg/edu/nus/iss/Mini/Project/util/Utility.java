package sg.edu.nus.iss.Mini.Project.util;

import org.springframework.beans.factory.annotation.Value;

public class Utility {
    @Value("${spring.data.api}")
    public static String apiKey;
    public static String concertUrl = "https://app.ticketmaster.com/discovery/v2/events.json?classificationName=music&apikey=";
}
