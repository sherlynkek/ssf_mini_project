package sg.edu.nus.iss.Mini.Project.controller;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.iss.Mini.Project.model.Event;
import sg.edu.nus.iss.Mini.Project.service.EventFilterService;
import sg.edu.nus.iss.Mini.Project.service.EventService;

@Controller
@RequestMapping("/event")
public class EventController {
    
    @Autowired
    EventService eventService;
    
    @Autowired
    EventFilterService eventFilterService;

    @GetMapping("/home")
    public String showEventHomePage(Model model) {
        model.addAttribute("eventsList", eventService.getAllEvent());

        return "eventHome";
    }

    @GetMapping("/{id}")
    public String getEventDetail(@PathVariable("id") String id, Model model) {

        Event events = eventService.getEventFromId(id);
        model.addAttribute("event", events);

        return "eventDetail";
    }

    @GetMapping("/filter")
public String showEvents(@RequestParam(name = "classification", required = false) String classification,
                         @RequestParam(name = "sortOption", required = false) String sortOption,
                         Model model) {
    // Call the service layer with the filter
    List<Event> filteredEvents = eventFilterService.getFilterEvent(classification);

    // Apply sorting only if a valid sort option is provided
    if (sortOption != null && !sortOption.isEmpty()) {
        switch (sortOption) {
            case "price-low-high":
                filteredEvents.sort(Comparator.comparing(Event::getTicketPriceLow, Comparator.nullsLast(Double::compareTo)));
                break;
            case "price-high-low":
                filteredEvents.sort(Comparator.comparing(Event::getTicketPriceLow, Comparator.nullsLast(Double::compareTo)).reversed());
                break;
            case "name-a-z":
                filteredEvents.sort(Comparator.comparing(Event::getAttractionName, Comparator.nullsLast(String::compareToIgnoreCase)));
                break;
            case "name-z-a":
                filteredEvents.sort(Comparator.comparing(Event::getAttractionName, Comparator.nullsLast(String::compareToIgnoreCase)).reversed());
                break;
        }
    }

    // Add data to the model
    model.addAttribute("events", filteredEvents);
    model.addAttribute("classifications", getAvailableClassifications(filteredEvents));
    model.addAttribute("sortOption", sortOption);

    // Return the view
    return "eventHome";
}

    // Helper method to extract available classifications from the filtered events (if needed)
        private List<String> getAvailableClassifications(List<Event> events) {
            return events.stream()
                    .map(Event::getClassificationName)
                    .distinct()
                    .collect(Collectors.toList());
        }
    }

    // @GetMapping("/myEvent")
    // public String getMyEventPage(Model model) {

    //     return "myEvent";
    // }
    
