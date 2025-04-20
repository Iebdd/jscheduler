package project.scheduler.Controller;


import java.time.Instant;
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
import project.scheduler.Services.BookingService.Status;
import project.scheduler.Services.CourseService;
import project.scheduler.Services.InscriptionService;
import project.scheduler.Services.PermissionService;
import project.scheduler.Services.PermissionService.Permissions;
import project.scheduler.Services.RoomService;
import project.scheduler.Services.UserService;
import project.scheduler.Tables.Course;
import project.scheduler.Tables.Room;
import project.scheduler.Tables.User;
import project.scheduler.Util.Password;
import project.scheduler.Util.UserBooking;
import project.scheduler.Util.UserToken;

/**
 * Controller class responsible for creating new entities within the database
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path="/add")
public class CreateController {

  @Inject
  private InscriptionService inscriptionService;
  @Inject
  private PermissionService permissionService;
  @Inject
  private UserService userService;
  @Inject
  private CourseService courseService;
  @Inject
  private RoomService roomService;
  @Inject
  private BookingService bookingService;

  /**
   * Endpoint to create a new user
   * 
   * @param role      The role of the new user (DEBUG)
   * @param firstName The new user's first name
   * @param lastName  The new user's last name
   * @param password  The new user's password
   * @param email     The new user's email address (Must be unique)
   * 
   * @return  A UserToken object, containing a token associated with the new user (To be saved locally)
   */
  @PostMapping(path="/user")
  public ResponseEntity<UserToken> addNewUser (@RequestParam String role, @RequestParam String firstName, 
                                               @RequestParam String lastName, @RequestParam String password, 
                                               @RequestParam String email) {
    if (userService.checkByEmail(email)) {
      return new ResponseEntity<>(new UserToken(), HttpStatus.CONFLICT);
    }
    Password pw = new Password(password);
    User user = new User(Integer.valueOf(role), firstName, lastName, pw.getPassword(), email);
    return userService.create(user);
  }

  /**
   *  Endpoint to request a new token (If the server stores a token which isn't available locally)
   * 
   * @param email     The email of the user in question
   * @param password  The password of the user in question
   * 
   * @return  A UserToken object containing a newly created token
   */
  @PostMapping(path="/token")
  public ResponseEntity<UserToken> addToken(@RequestParam String email, @RequestParam String password) {
    User user = userService.findUserByEmail(email);
    if(user == null) {
      return new ResponseEntity<>(new UserToken(), HttpStatus.UNAUTHORIZED);
    }
    if(permissionService.validPassword(password, user.getPassword())) {
      permissionService.cullTokens();
      return permissionService.setToken(user);
    }
    try {
      Thread.sleep(1000);         // Server waits one second before returning a negative result
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    return new ResponseEntity<>(new UserToken(), HttpStatus.UNAUTHORIZED);
  }

  /**
   * Endpoint to create a new course
   * 
   * @param courseName  The name of the new course
   * @param header      A Bearer Token containing an authentication token (Authorization: Bearer {token}) Token contains {@value PermissionService#TOKEN_LENGTH} upper or lower case letters, or numbers <p>
   *                    Requires Admin level permissions
   * 
   * @return  An Object representing the newly created course
   */
  @PostMapping(path="/course")
  public ResponseEntity<Course> addCourse (@RequestParam String courseName, @RequestHeader("Authorization") String header) {
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {
      return new ResponseEntity<>(new Course(), HttpStatus.UNAUTHORIZED);
    }
    if(permissionService.validRole(token, Permissions.Admin)) {
      return courseService.create(courseName);
    } else {
      return new ResponseEntity<>(new Course(), HttpStatus.UNAUTHORIZED);
    }
  }



  /**
   * Endpoint to create a new room
   * 
   * @param roomName    The name of the new room
   * @param header      A Bearer Token containing an authentication token (Authorization: Bearer {token}) Token contains {@value PermissionService#TOKEN_LENGTH} upper or lower case letters, or numbers <p>
   *                    Requires Admin level permissions
   * 
   * @return  An Object representing the newly created room
   */
  @PostMapping(path="/room")
  public ResponseEntity<Room> addRoom(@RequestParam String roomName, @RequestHeader("Authorization") String header) {
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {
      return new ResponseEntity<>(new Room(), HttpStatus.UNAUTHORIZED);
    }
    if(permissionService.validRole(token, Permissions.Admin)) {
      return roomService.create(roomName);
    } else {
      return new ResponseEntity<>(new Room(), HttpStatus.UNAUTHORIZED);
    }
  }

  /**
   * Endpoint to create a new booking (A course taking place in a room with a set start and end date)
   * 
   * @param course_id   The id of the course to be booked - ID is a HEX number in the format of (DDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD)
   * @param room_id     The id of the room to be booked - ID is a HEX number in the format of (DDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD)
   * @param start       The start time of the course - Time in the format of (YYYY-MM-DDThh:mm:ssZ)
   * @param end         The end time of the course - Time in the format of (YYYY-MM-DDThh:mm:ssZ)
   * @param header      A Bearer Token containing an authentication token (Authorization: Bearer {token}) Token contains {@value PermissionService#TOKEN_LENGTH} upper or lower case letters, or numbers <p>
   *                    Requires at least Assistant level permissions
   * 
   * @return  A UserBooking object containing either the newly created booking_id or the ids of courses conflicting with this booking 
   * (The same course taking place in another room : time_conflict / Another course taking place in the same room at the same time: room_conflict)
   */
  @PostMapping(path="/booking")
  public ResponseEntity<UserBooking> bookRoom(@RequestParam UUID course_id, @RequestParam UUID room_id, @RequestParam Instant start, @RequestParam Instant end, @RequestHeader("Authorization") String header) {
    Status status = Status.Planned;
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {
      return new ResponseEntity<>(new UserBooking(), HttpStatus.UNAUTHORIZED);
    }
    if(permissionService.validRole(token, Permissions.Admin)) {
      status = Status.Set;
    } else if(!permissionService.validRole(token, Permissions.Assistant)) {
      return new ResponseEntity<>(new UserBooking(), HttpStatus.UNAUTHORIZED);
    }
    Course course = courseService.findCourseById(course_id).orElse(null); 
    Room room = roomService.findRoomById(room_id).orElse(null);
    if(room == null || course == null) {
      return new ResponseEntity<>(new UserBooking(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    return bookingService.setBooking(course, room, start, end, status);
  }

  /**
   * Endpoint to create a new preference for a course (That a course should take place in a room with a set start and end date but to be confirmed)
   * 
   * @param course_id   The id of the course to be booked -  ID is a HEX number in the format of (DDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD)
   * @param room_id     The id of the room to be booked - ID is a HEX number in the format of (DDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD)
   * @param start       The start time of the course - Time in the format of (YYYY-MM-DDThh:mm:ssZ)
   * @param end         The end time of the course - Time in the format of (YYYY-MM-DDThh:mm:ssZ)
   * @param header      A Bearer Token containing an authentication token (Authorization: Bearer {token}) Token contains {@value PermissionService#TOKEN_LENGTH} upper or lower case letters, or numbers <p>
   *                    Requires at least Assistant level permissions
   * 
   * @return  A UserBooking object containing either the newly created booking_id or the ids of courses conflicting with this booking  <p>
   * (The same course taking place in another room : time_conflict / Another course taking place in the same room at the same time: room_conflict)
   */
  @PostMapping(path="/preference")
  public ResponseEntity<UserBooking> setPreference(@RequestParam UUID course_id, @RequestParam UUID room_id, @RequestParam Instant start, @RequestParam Instant end, @RequestHeader("Authorization") String header) {
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {
      return new ResponseEntity<>(new UserBooking(), HttpStatus.UNAUTHORIZED);
    }
    if(!permissionService.validRole(token, Permissions.Assistant)) {
      return new ResponseEntity<>(new UserBooking(), HttpStatus.UNAUTHORIZED);
    }
    Course course = courseService.findCourseById(course_id).orElse(null); 
    Room room = roomService.findRoomById(room_id).orElse(null);
    if(room == null || course == null) {
      return new ResponseEntity<>(new UserBooking(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    return bookingService.setBooking(course, room, start, end, Status.Preference);
  }

  /**
   * Endpoint to inscribe either another student (As an Assistant or Admin) or yourself into a course (As a Student)
   * 
   * @param user_id     The id of the user to be inscribed -  ID is a HEX number in the format of (DDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD)
   * @param course_id   The id of the course to be inscribed into -  ID is a HEX number in the format of (DDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD)
   * @param header      A Bearer Token containing an authentication token (Authorization: Bearer {token}) Token contains {@value PermissionService#TOKEN_LENGTH} upper or lower case letters and number <p>
   *                    Requires at least Assistant level permissions or Student level permissions for the student in question
   * 
   * @return  A UserBooking object containing courses conflicting with the current inscription (Courses the student is inscribed to that take place in another room at the same time)
   */
  @PostMapping(path="/inscription")
  public ResponseEntity<UserBooking> inscribeStudent(@RequestParam UUID user_id, @RequestParam UUID course_id, @RequestHeader("Authorization") String header) {
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {
      return new ResponseEntity<>(new UserBooking(), HttpStatus.UNAUTHORIZED);
    }
    if(!permissionService.validRole(token, user_id, Permissions.Assistant)) {
      return new ResponseEntity<>(new UserBooking(), HttpStatus.UNAUTHORIZED);
    }
    if(inscriptionService.ifExists(user_id, course_id)) {
      return new ResponseEntity<>(new UserBooking(), HttpStatus.CONFLICT);
    }
    User user = userService.findUserById(user_id).orElse(null);
    Course course = courseService.findCourseById(course_id).orElse(null);
    try {
      return ResponseEntity.ok(inscriptionService.inscribe(user, course));
    } catch (NullPointerException e) {
      return new ResponseEntity<>(new UserBooking(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }
}
