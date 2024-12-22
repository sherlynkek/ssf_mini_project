package sg.edu.nus.iss.Mini.Project.controller;

import java.util.Arrays;
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
                             Model model) {
        
        // Call the service layer with the classification filter
        List<Event> filteredEvents = eventFilterService.getFilterEvent(classification);
    
        // Add the filtered events to the model to display in the view
        model.addAttribute("events", filteredEvents);
        
        // Add filter options to the model to be used in the form
        model.addAttribute("classifications", getAvailableClassifications(filteredEvents));
        
        return "eventHome"; // The name of your Thymeleaf template to display the events
        
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
    
