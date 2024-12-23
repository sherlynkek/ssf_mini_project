package sg.edu.nus.iss.Mini.Project.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    // Fetch all events initially
    List<Event> allEvents = eventService.getAllEvent();
    List<Event> filteredEvents = new ArrayList<>();

    // Apply classification filter
    if (classification != null && !classification.isEmpty()) {
        for (Event event : allEvents) {
            if (classification.equals(event.getClassificationName())) {
                filteredEvents.add(event);
            }
        }
    } 
    else {
        filteredEvents = allEvents; // If no classification filter, show all events
    }
        // Apply sorting only if a valid sort option is provided
        if (sortOption != null && !sortOption.isEmpty()) {
            switch (sortOption) {
                case "price-low-high":
                    filteredEvents.sort(new Comparator<Event>() {
                        @Override
                        public int compare(Event e1, Event e2) {
                            Double price1 = e1.getTicketPriceLow();
                            Double price2 = e2.getTicketPriceLow();
                            if (price1 == null && price2 == null) {
                                return 0;
                            } else if (price1 == null) {
                                return 1; // Nulls last
                            } else if (price2 == null) {
                                return -1; // Nulls last
                            } else {
                                return price1.compareTo(price2);
                            }
                        }
                    });
                    break;
        
                case "price-high-low":
                    filteredEvents.sort(new Comparator<Event>() {
                        @Override
                        public int compare(Event e1, Event e2) {
                            Double price1 = e1.getTicketPriceLow();
                            Double price2 = e2.getTicketPriceLow();
                            if (price1 == null && price2 == null) {
                                return 0;
                            } else if (price1 == null) {
                                return 1; // Nulls last
                            } else if (price2 == null) {
                                return -1; // Nulls last
                            } else {
                                return price2.compareTo(price1); // Reversed order
                            }
                        }
                    });
                    break;
        
                case "name-a-z":
                    filteredEvents.sort(new Comparator<Event>() {
                        @Override
                        public int compare(Event e1, Event e2) {
                            String name1 = e1.getAttractionName();
                            String name2 = e2.getAttractionName();
                            if (name1 == null && name2 == null) {
                                return 0;
                            } else if (name1 == null) {
                                return 1; // Nulls last
                            } else if (name2 == null) {
                                return -1; // Nulls last
                            } else {
                                return name1.compareToIgnoreCase(name2);
                            }
                        }
                    });
                    break;
        
                case "name-z-a":
                    filteredEvents.sort(new Comparator<Event>() {
                        @Override
                        public int compare(Event e1, Event e2) {
                            String name1 = e1.getAttractionName();
                            String name2 = e2.getAttractionName();
                            if (name1 == null && name2 == null) {
                                return 0;
                            } else if (name1 == null) {
                                return 1; // Nulls last
                            } else if (name2 == null) {
                                return -1; // Nulls last
                            } else {
                                return name2.compareToIgnoreCase(name1); // Reversed order
                            }
                        }
                    });
                    break;
        
                default:
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