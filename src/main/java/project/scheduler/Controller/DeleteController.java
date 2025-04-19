package project.scheduler.Controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import project.scheduler.Services.BookingService;
import project.scheduler.Services.InscriptionService;
import project.scheduler.Services.PermissionService;
import project.scheduler.Services.PermissionService.Permissions;

/**
 * Controller class responsible for removing entities from the database
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path="/remove")
public class DeleteController {

  @Inject
  private InscriptionService inscriptionService;
  @Inject
  private PermissionService permissionService;
  @Inject
  private BookingService bookingService;

  /**
   * Endpoint to remove either another student (As an Assistant or Admin) or yourself from a course (As a Student)
   * 
   * @param user_id     The id of the user to be removed -  ID is a HEX number in the format of (DDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD)
   * @param course_id   The id of the course to be removed from -  ID is a HEX number in the format of (DDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD)
   * @param header      A Bearer Token containing an authentication token (Authorization: Bearer {token}) Token contains {@value PermissionService#TOKEN_LENGTH} upper or lower case letters and number <p>
   *                    Requires at least Assistant level permissions or Student level permissions for the student in question
   * 
   * @return  A String containing feedback on the operation (Refer to response codes for response handling)
   */
  @PostMapping(path="/inscription")
  public ResponseEntity<String> removeStudent(@RequestParam UUID user_id, @RequestParam UUID course_id, @RequestHeader("Authorization") String header) {
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {
      return new ResponseEntity<>("Invalid Authorization Header", HttpStatus.UNAUTHORIZED);
    }
    if(!permissionService.validRole(token, user_id, Permissions.Assistant)) {
      return new ResponseEntity<>("Insufficient permissions", HttpStatus.UNAUTHORIZED);
    }
    if(!inscriptionService.ifExists(user_id, course_id)) {
      return new ResponseEntity<>(String.format("User: %s is not inscribed in Course: %s", user_id, course_id), HttpStatus.BAD_REQUEST);
    }
    return inscriptionService.remove(user_id, course_id);
  }

  /**
   *  Endpoint to remove a booking (A course taking place in a room with a set start and end date)
   * 
   * @param booking_id  The id of the course to be removed - ID is a HEX number in the format of (DDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD)
   * @param header      A Bearer Token containing an authentication token (Authorization: Bearer {token}) Token contains {@value PermissionService#TOKEN_LENGTH} upper or lower case letters and number <p>
   *                    Requires at least Admin level permissions
   * @return  An integer representation of the result. 1 means the booking was removed successfully / 0 means it wasn't and nothing has changed (Missing permissions or invalid booking_id)
   */
  @PostMapping(path="/booking")
    public ResponseEntity<Integer> removeBooking(@RequestParam UUID booking_id, @RequestHeader("Authorization") String header) {
      String token = permissionService.validAuthHeader(header);
      if(token.length() == 0) {
        return new ResponseEntity<>(0, HttpStatus.UNAUTHORIZED);
      }
      if(!permissionService.validRole(token, Permissions.Admin)) {
        return new ResponseEntity<>(0, HttpStatus.UNAUTHORIZED);
      }
      return ResponseEntity.ok(bookingService.removeBookingById(booking_id));
    }

}
