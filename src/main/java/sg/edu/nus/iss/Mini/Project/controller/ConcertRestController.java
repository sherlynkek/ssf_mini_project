package sg.edu.nus.iss.Mini.Project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.Mini.Project.model.Concert;
import sg.edu.nus.iss.Mini.Project.service.ConcertRestService;

@RestController
@RequestMapping("/api/concert")
public class ConcertRestController {
    
    @Autowired
    ConcertRestService concertRestService;
    
    @GetMapping("")
    public ResponseEntity<List<Concert>> getAllConcert() {
        List<Concert> concert = concertRestService.getAllConcert();
        
        return ResponseEntity.ok().body(concert);
    }
}
