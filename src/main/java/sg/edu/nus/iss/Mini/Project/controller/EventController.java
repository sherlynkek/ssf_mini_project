package sg.edu.nus.iss.Mini.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.Mini.Project.model.Event;
import sg.edu.nus.iss.Mini.Project.service.EventService;

@Controller
@RequestMapping("/event")
public class EventController {
    
    @Autowired
    EventService eventService;

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

    @GetMapping("/location")
    public String getEventLocation(Model model) {
        
        return "";
    }

    @GetMapping("/myEvent")
    public String getMyEventPage(Model model) {

        return "myEvent";
    }
    
}
