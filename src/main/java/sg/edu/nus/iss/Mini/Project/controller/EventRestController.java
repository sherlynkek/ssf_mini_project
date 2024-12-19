package sg.edu.nus.iss.Mini.Project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.Mini.Project.model.Event;
import sg.edu.nus.iss.Mini.Project.service.EventRestService;

@RestController
@RequestMapping("/api/event")
public class EventRestController {
    
    @Autowired
    EventRestService eventRestService;
    
    @GetMapping("")
    public ResponseEntity<List<Event>> getAllEvent() {
        List<Event> event = eventRestService.getAllEvent();
        
        return ResponseEntity.ok().body(event);
    }
}
