package sg.edu.nus.iss.Mini.Project.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // shows all the events available 
    @GetMapping("/home")
    public String showEventHomePage(Model model) {
        List<Event> eventsList = eventService.getAllEvent();

        // Sort events by date (ascending order)
        eventsList.sort(Comparator.comparing(Event::getDate));

        model.addAttribute("eventsList", eventsList);

        return "eventHome";
    }

    // after clicking into the specific event, it will show the event details
    @GetMapping("/{id}")
    public String getEventDetail(@PathVariable("id") String id, Model model) {

        Event events = eventService.getEventFromId(id);
        model.addAttribute("event", events);

        return "eventDetail";
    }

    // filter the events by their classification and sort them by price and event name
    @GetMapping("/filter")
    public String showEvents(@RequestParam(name = "classification", required = false) String classification,
                            @RequestParam(name = "sortOption", required = false) String sortOption,
                            @RequestParam(name = "searchQuery", required = false) String searchQuery,
                            Model model) {
        // Fetch all events initially
        List<Event> allEvents = eventService.getAllEvent();
        List<Event> filteredEvents = new ArrayList<>();

        // Apply classification filter
        for(Event event : allEvents) {
            if(classification == null || classification.isEmpty() || 
                classification.equals(event.getClassificationName())) {
                
                filteredEvents.add(event);
            }
        }

        // Apply search filter
        if(searchQuery != null && !searchQuery.isEmpty()) {
            List<Event> searchFilteredEvents = new ArrayList<>();
            for(Event event : filteredEvents) {
                if(event.getAttractionName() != null && 
                   event.getAttractionName().toLowerCase().contains(searchQuery.toLowerCase())) {
                
                    searchFilteredEvents.add(event);
                }
            }
            filteredEvents = searchFilteredEvents;
        }

        // Apply sorting
        if(sortOption != null && !sortOption.isEmpty()) {
            // Apply selected sort option
            switch(sortOption) {
                case "price-low-high":
                    filteredEvents.sort(new Comparator<Event>() {
                        @Override
                        public int compare(Event e1, Event e2) {
                            Double price1 = e1.getTicketPriceLow();
                            Double price2 = e2.getTicketPriceLow();
                            if(price1 == null && price2 == null) return 0;
                            if(price1 == null) return 1; // Nulls last
                            if(price2 == null) return -1; // Nulls last
                            
                            return price1.compareTo(price2);
                        }
                    });
                    break;

                case "price-high-low":
                    filteredEvents.sort(new Comparator<Event>() {
                        @Override
                        public int compare(Event e1, Event e2) {
                            Double price1 = e1.getTicketPriceLow();
                            Double price2 = e2.getTicketPriceLow();
                            if(price1 == null && price2 == null) return 0;
                            if(price1 == null) return 1; // Nulls last
                            if(price2 == null) return -1; // Nulls last
                            
                            return price2.compareTo(price1); // Reversed order
                        }
                    });
                    break;

                case "name-a-z":
                    filteredEvents.sort(new Comparator<Event>() {
                        @Override
                        public int compare(Event e1, Event e2) {
                            String name1 = e1.getAttractionName();
                            String name2 = e2.getAttractionName();
                            if(name1 == null && name2 == null) return 0;
                            if(name1 == null) return 1; // Nulls last
                            if(name2 == null) return -1; // Nulls last
                            
                            return name1.compareToIgnoreCase(name2);
                        }
                    });
                    break;

                case "name-z-a":
                    filteredEvents.sort(new Comparator<Event>() {
                        @Override
                        public int compare(Event e1, Event e2) {
                            String name1 = e1.getAttractionName();
                            String name2 = e2.getAttractionName();
                            if(name1 == null && name2 == null) return 0;
                            if(name1 == null) return 1; // Nulls last
                            if(name2 == null) return -1; // Nulls last
                            
                            return name2.compareToIgnoreCase(name1); // Reversed order
                        }
                    });
                    break;

                default:
                    break;
            }
        } 
        else {
            // Default sorting by date (closest first) if no sort option is selected
            filteredEvents.sort(new Comparator<Event>() {
                @Override
                public int compare(Event e1, Event e2) {
                    if(e1.getDate() == null && e2.getDate() == null) return 0;
                    if(e1.getDate() == null) return 1; // Nulls last
                    if(e2.getDate() == null) return -1; // Nulls last
                    
                    return e1.getDate().compareTo(e2.getDate());
                }
            });
        }

        // Add data to the model
        model.addAttribute("events", filteredEvents);
        model.addAttribute("classifications", getAvailableClassifications(allEvents));
        model.addAttribute("sortOption", sortOption);
        model.addAttribute("searchQuery", searchQuery);

        // Return the view
        return "eventHome";
    }

    private Set<String> getAvailableClassifications(List<Event> events) {
        Set<String> classifications = new HashSet<>();
        for(Event event : events) {
            if(event.getClassificationName() != null) {
                classifications.add(event.getClassificationName());
            }
        }
        return classifications;
    }
}