package rocketseat.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.checkin.CheckIn;
import rocketseat.com.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import rocketseat.com.passin.repositories.CheckinRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckinService {
    private final CheckinRepository checkinRepository;


    public void registerCheckIn(Attendee attendee) {
        this.verifyCheckInExists(attendee.getId());
        CheckIn checkIn = new CheckIn();
        checkIn.setAttendee(attendee);
        checkIn.setCreatedAt(LocalDateTime.now());
        this.checkinRepository.save(checkIn);
    }

    public Optional<CheckIn> getByAttendeeId(String attendeeId) {
       Optional<CheckIn> checkin =  this.checkinRepository.findByAttendeeId(attendeeId);
       return checkin;
    }

    private void verifyCheckInExists(String attendeeId) {
        Optional<CheckIn> checkInExists = this.checkinRepository.findByAttendeeId(attendeeId);
        if(checkInExists.isPresent()) {
            throw new CheckInAlreadyExistsException("Attendee already checked on.");
        }
    }
}
