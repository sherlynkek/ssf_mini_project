package sg.edu.nus.iss.Mini.Project.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import sg.edu.nus.iss.Mini.Project.model.Event;
import sg.edu.nus.iss.Mini.Project.service.EventService;

@Controller

public class CalendarController {

    @Autowired
    EventService eventService;
    
    @GetMapping("/event/{eventId}/download-calendar")
    public ResponseEntity<byte[]> downloadCalendar(@PathVariable String eventId) {
        Event event = eventService.getEventFromId(eventId); // Fetch event details

        // Use LocalDate to get only the date (no time)
        LocalDate eventDate = event.getDate();
        
        // Format the event date as an all-day event (no time)
        String icsContent = "BEGIN:VCALENDAR\n" +
                "VERSION:2.0\n" +
                "BEGIN:VEVENT\n" +
                "UID:" + event.getId() + "\n" +
                "DTSTAMP:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")) + "\n" +
                "DTSTART;VALUE=DATE:" + eventDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "\n" +
                "DTEND;VALUE=DATE:" + eventDate.plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "\n" +  // The event ends the next day, signifying an all-day event
                "SUMMARY:" + event.getEventName() + "\n" +
                "LOCATION:" + event.getVenueName() + "\n" +
                "DESCRIPTION:Enjoy the event!\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR";

        byte[] bytes = icsContent.getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=event.ics")
                .contentType(MediaType.parseMediaType("text/calendar"))
                .body(bytes);
    }
}