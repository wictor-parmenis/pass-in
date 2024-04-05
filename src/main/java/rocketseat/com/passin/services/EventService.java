package rocketseat.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.event.Event;
import rocketseat.com.passin.domain.event.exceptions.EventNotFoundException;
import rocketseat.com.passin.dto.event.EventIdDTO;
import rocketseat.com.passin.dto.event.EventRequestDTO;
import rocketseat.com.passin.dto.event.EventResponseDTO;
import rocketseat.com.passin.repositories.AttendeeRepository;
import rocketseat.com.passin.repositories.EventRepository;

import java.text.Normalizer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetail(String eventId) {
        Event event = this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with id:" + eventId));
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventResponseDTO(event,attendeeList.size() );
    }

    public EventIdDTO createEvent(EventRequestDTO eventDTO) {
        System.out.println("Entrei no service.");
        Event newEvent = new Event();

        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        System.out.println("antes do createSlug." );

        newEvent.setSlug(this.createSlug(eventDTO.title()));
        System.out.println("depois do createSlug." );

        this.eventRepository.save(newEvent);
        System.out.println("passei do save event no service." + newEvent);

        return new EventIdDTO(newEvent.getId());
    }

    private String createSlug(String eventTitle) {
      String normalizedText  = Normalizer.normalize(eventTitle, Normalizer.Form.NFD);
      return normalizedText.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
              .replaceAll("[^\\w\\s]", "")
              .replaceAll("\\s+", "")
              .toLowerCase();

    };
}
