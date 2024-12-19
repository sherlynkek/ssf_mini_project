package sg.edu.nus.iss.Mini.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.Mini.Project.model.Event;
import sg.edu.nus.iss.Mini.Project.service.EventRestService;

@Controller
@RequestMapping("/event")
public class EventController {
    
    @Autowired
    EventRestService eventRestService;

    @GetMapping("/home")
    public String showEventHomePage(Model model) {
        model.addAttribute("eventsList", eventRestService.getAllEvent());
        // Event event = eventRestService.getAllEvent().get(0);
        // model.addAttribute("event", event);
        // for(int i = 0; i<eventRestService.getAllEvent().size(); i++){
        //     System.out.println(eventRestService.getAllEvent().get(i).getImageUrl());
        // }
        
        return "eventHome";
    }

    @GetMapping("/eventName")
    public String showEventPage(Model model) {
        return "eventName";
    }
}
