package sg.edu.nus.iss.Mini.Project.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.stream.EventFilter;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import sg.edu.nus.iss.Mini.Project.model.Event;


@Service
public class EventFilterService {
    
    @Autowired
    EventService eventService;
    
    public List<Event> getEvent() {
        
        return eventService.getAllEvent();
        
    }

    public Map<String, List<Event>> categorizeEvent(List<Event> events) {
        return events.stream()
                .collect(Collectors.groupingBy(Event::getVenueName)); // Adjust based on your category logic
    }

}
