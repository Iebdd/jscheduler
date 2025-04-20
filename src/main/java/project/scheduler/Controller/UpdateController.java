package project.scheduler.Controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import project.scheduler.Services.BookingService;
import project.scheduler.Services.BookingService.Status;
import project.scheduler.Services.PermissionService;
import project.scheduler.Services.PermissionService.Permissions;
import project.scheduler.Services.UserService;
import project.scheduler.Tables.User;
import project.scheduler.Util.Password;

/**
 * Controller class responsible for removing entities from the database
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path="/update")
public class UpdateController {

  @Inject
  private PermissionService permissionService;
  @Inject
  private UserService userService;
  @Inject
  private BookingService bookingService;

  /**
   * Sets a new password for the given user by providing the old password as proof
   * 
   * @param old_password  The password the user has before the change to be validated
   * @param new_password  The new password for the user
   * @param user_id       The user to have the password changed
   * 
   * @return  A String informing of the success or failure of the request
   */
  @PatchMapping(path="/password/verify")
  public ResponseEntity<String> setPasswordById(@RequestParam String old_password, @RequestParam String new_password, @RequestParam UUID user_id) {
      User new_user = userService.findUserById(user_id).orElse(null);
      if (new_user == null || !Password.compare(old_password, new_user.getPassword())) {
        return new ResponseEntity<>(String.format("User %s not found or password incorrect", user_id), HttpStatus.UNPROCESSABLE_ENTITY);
      }
      permissionService.setPassword(new_password, user_id);
      return ResponseEntity.ok(String.format("Saved new password for user: %s", user_id));
  }

  /**
   * Set a new password for the given user by providing a token as security
   * 
   * @param new_password The new password for the user
   * @param user_id      The user to have the password changed
   * @param header       A Bearer Token containing an authentication token (Authorization: Bearer {token}) Token contains {@value PermissionService#TOKEN_LENGTH} upper or lower case letters, or numbers <p>
   *                     Requires Admin level permissions or Student level permissions for the student in question    
   * 
   * @return  A String informing of the success or failure of the request
   */
  @PatchMapping(path="/password/token")
  public ResponseEntity<String> setPasswordByToken(@RequestParam String new_password, @RequestParam UUID user_id, @RequestHeader("Authorization") String header) {
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {
      return new ResponseEntity<>("Invalid Authorization Header", HttpStatus.UNAUTHORIZED);
    }
    if(!permissionService.validRole(token, user_id, Permissions.Admin)) {
      return new ResponseEntity<>("Insufficient Permission", HttpStatus.UNAUTHORIZED);
    }
    permissionService.setPassword(new_password, user_id);
    return ResponseEntity.ok(String.format("Saved new password for user: %s", user_id));
  }

  /**
   * Set a different status for a given booking
   * 
   * @param new_status  The new status to be changed to
   * @param booking_id  The id of the booking in question - ID is a HEX number in the format of (DDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD)
   * @param header       A Bearer Token containing an authentication token (Authorization: Bearer {token}) Token contains {@value PermissionService#TOKEN_LENGTH} upper or lower case letters, or numbers <p>
   *                     Requires Admin level permissions or Student level permissions for the student in question 
   *     
   * @return  An integer representation of the result. 1 means the booking was altered successfully / 0 means it wasn't and nothing has changed (Missing permissions or invalid booking_id)
   */
  @PatchMapping(path="/bookingStatus")
  public ResponseEntity<Integer> confirmBooking(@RequestParam Status new_status, @RequestParam UUID booking_id, @RequestHeader("Authorization") String header) {
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {
      return new ResponseEntity<>(0, HttpStatus.UNAUTHORIZED);
    }
    if(!permissionService.validRole(token, Permissions.Admin)) {
      return new ResponseEntity<>(0, HttpStatus.UNAUTHORIZED);
    }
    return bookingService.updateBookingStatus(new_status, booking_id);
  }

  /**
   * Refreshes the expiry date of a token
   * 
   * @param user_id The id of the user the token is associated with
   * @param header       A Bearer Token containing an authentication token (Authorization: Bearer {token}) Token contains {@value PermissionService#TOKEN_LENGTH} upper or lower case letters, or numbers <p>
   *                     Requires the token to be associated with the passed user
   * 
   * @return  True if the token was refreshed successfully, false if not
   */
  @PatchMapping(path="/token")
  public ResponseEntity<Boolean> refreshToken(@RequestParam UUID user_id, @RequestHeader("Authorization") String header) {
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {
      return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
    }
    return permissionService.refreshToken(user_id, token);
  }
  
}
