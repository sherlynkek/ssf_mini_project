package sg.edu.nus.iss.Mini.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import sg.edu.nus.iss.Mini.Project.service.ConcertRestService;

@Controller
public class ConcertRestController {
    
    @Autowired
    ConcertRestService concertRestService;
    
}
