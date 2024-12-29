package sg.edu.nus.iss.Mini.Project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.Mini.Project.model.Event;
import sg.edu.nus.iss.Mini.Project.service.EventFilterService;
import sg.edu.nus.iss.Mini.Project.service.EventService;


@RestController
@RequestMapping("/api/event")
public class EventRestController {

    @Autowired
    EventService eventService;

    @Autowired
    EventFilterService eventFilterService;
    
    // Endpoint to get all events
    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvent() {
        List<Event> event = eventService.getAllEvent();
        return ResponseEntity.ok().body(event);
    }

    // Endpoint to get events filtered by classification (e.g., "Music", "Sports", etc.)
    @GetMapping("/class")
    public ResponseEntity<List<Event>> getInterestedEventsByClassification(
            @RequestParam(value = "classification", defaultValue = "Music") String classification) {
        List<Event> interestedEvents = eventFilterService.getFilterEvent(classification);
        return ResponseEntity.ok().body(interestedEvents);
    }

    // Endpoint to get events based on a list of event IDs
    @GetMapping("/id")
    public ResponseEntity<List<Event>> getInterestedEventsByIds(
            @RequestParam(value = "eventIds", required = false) List<String> eventIds) {

        if (eventIds == null || eventIds.isEmpty()) {
            // Handle the case when no event IDs are provided (optional behavior)
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }

        List<Event> interestedEvents = eventService.getEventsByIds(eventIds);
        return ResponseEntity.ok().body(interestedEvents);
    }


}
