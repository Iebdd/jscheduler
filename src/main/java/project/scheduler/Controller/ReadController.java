package project.scheduler.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import project.scheduler.Services.BookingService;
import project.scheduler.Services.CourseService;
import project.scheduler.Services.InscriptionService;
import project.scheduler.Services.PermissionService;
import project.scheduler.Services.PermissionService.Permissions;
import project.scheduler.Services.RoomService;
import project.scheduler.Services.UserService;
import project.scheduler.Tables.Booking;
import project.scheduler.Tables.Course;
import project.scheduler.Tables.Room;
import project.scheduler.Util.StringUtil;
import project.scheduler.Util.TimeUtil;


/**
 * Controller class responsible for removing entities from the database
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path="/read")
public class ReadController {
  @Inject
  private CourseService courseService;
  @Inject
  private PermissionService permissionService;
  @Inject
  private BookingService bookingService;
  @Inject
  private RoomService roomService;
  @Inject
  private UserService userService;
  @Inject
  private InscriptionService inscriptionService;

  /**
   *  Returns an Iterable containing all Rooms currently in the database
   * 
   * @return  An Iterable of all rooms
   */
  @GetMapping(path="/rooms")
  public ResponseEntity<Iterable<Room>> getAllRooms() {
    return roomService.findAllRooms();
  }

  /**
   *  Returns an Iterable containing all Courses currently in the database
   * 
   * @return  An Iterable of all courses
   */
  @GetMapping(path="/courses")
  public ResponseEntity<Iterable<Course>> getAllCourses() {
    return ResponseEntity.ok(courseService.findAllCourses());
  }

  /**
   * Returns the ID of the of the passed room
   * 
   * @param name  The name of the sought after room id
   * 
   * @return  The id corresponding to the room name
   */
  @GetMapping(path="/room/{name}") 
  public ResponseEntity<String> getRoomIdByName(@PathVariable String name) {
    return ResponseEntity.ok(roomService.findRoomByName(name).getId().toString());
  }

  /**
   * Returns the ID of the of the passed course
   * 
   * @param name  The name of the sought after course id
   * 
   * @return  The id corresponding to the course name
   */
  @GetMapping(path="/course/{name}") 
  public ResponseEntity<String> getCourseIdByName(@PathVariable String name) {
    return ResponseEntity.ok(courseService.findCourseByName(name).getId().toString());
  }

  /**
   * Returns all courses taking place in a given room
   * 
   * @param room_id The id of the room in question - ID is a HEX number in the format of (DDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD)
   * 
   * @return  An Iterable of all Bookings within the room
   */
  @GetMapping(path="/roomBookings/{room_id}")
  public ResponseEntity<Iterable<Booking>> getBookedCourses(@PathVariable UUID room_id) {
    return bookingService.findAllBookingsByRoom(room_id);
  }

  /**
   *  Returns all courses taking place in a given room on a given day
   * 
   * @param room_id The id of the room in question - ID is a HEX number in the format of (DDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD)
   * @param date    The day in question in the format of YYYY-MM-DD
   * 
   * @return  An Iterable of all Bookings within the room on that day
   */
  @GetMapping(path="/roomBookings/{room_id}/{date}")
  public ResponseEntity<Iterable<UUID>> getBookedCoursesByDay(@PathVariable UUID room_id, @PathVariable LocalDate date) {
    return ResponseEntity.ok(bookingService.findAllIdsByRoomIdAndDate(room_id, date));
  }

  /**
   * Returns all courses the passed user is currently enrolled in
   * 
   * @param user_id The id of the user to be queried - ID is a HEX number in the format of (DDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD)
   * @param header      A Bearer Token containing an authentication token (Authorization: Bearer {token}) Token contains {@value PermissionService#TOKEN_LENGTH} upper or lower case letters, or numbers <p>
   *                    Requires Admin level permissions or Student level permissions for the student in question
   * 
   * @return  An Iterable containing all courses the user is enrolled in
   */
  @GetMapping(path="/userBookings/{user_id}")
  public ResponseEntity<String> getInscribedCourses(@PathVariable UUID user_id, @RequestHeader("Authorization") String header) {
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {
      return new ResponseEntity<>(new LinkedMultiValueMap<>(), HttpStatus.NO_CONTENT);
    }
    if(!permissionService.validRole(token, user_id, Permissions.Admin)) {
      return new ResponseEntity<>(new LinkedMultiValueMap<>(), HttpStatus.NO_CONTENT);
    }
    return ResponseEntity.ok(StringUtil.toString(bookingService.findAllBookingsByUser(user_id)));
  }

  /**
   * Returns all Bookings recorded
   * 
   * @param header     A Bearer Token containing an authentication token (Authorization: Bearer {token}) Token contains {@value PermissionService#TOKEN_LENGTH} upper or lower case letters, or numbers <p>
   *                   Requires Assistant level permissions
   * 
   * @return  An Iterable containing all Bookings
   */
  @GetMapping(path="/bookings")
  public ResponseEntity<String> getBookings(@RequestHeader("Authorization") String header) {
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {
      return new ResponseEntity<>(new LinkedMultiValueMap<>(), HttpStatus.NO_CONTENT);
    }
    if(!permissionService.validRole(token, Permissions.Assistant)) {
      return new ResponseEntity<>(new LinkedMultiValueMap<>(), HttpStatus.NO_CONTENT);
    }
    return ResponseEntity.ok(StringUtil.toString(bookingService.getAllBookings()));
  }

  /**
   *  Returns all Users without their passwords
   * 
   * @param header     A Bearer Token containing an authentication token (Authorization: Bearer {token}) Token contains {@value PermissionService#TOKEN_LENGTH} upper or lower case letters, or numbers <p>
   *                   Requires Assistant level permissions
   * 
   * @return  A JSON string containing all users
   */
  @GetMapping(path="/users")
  public ResponseEntity<String> getUsers(@RequestHeader("Authorization") String header) {
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {
      return new ResponseEntity<>(new LinkedMultiValueMap<>(), HttpStatus.NO_CONTENT);
    }
    if(!permissionService.validRole(token, Permissions.Assistant)) {
      return new ResponseEntity<>(new LinkedMultiValueMap<>(), HttpStatus.NO_CONTENT);
    }
    return ResponseEntity.ok(StringUtil.toString(userService.getAllUsers()));   //Omits the password
  }

  /**
   *  Returns all Inscriptions
   * 
   * @param header     A Bearer Token containing an authentication token (Authorization: Bearer {token}) Token contains {@value PermissionService#TOKEN_LENGTH} upper or lower case letters, or numbers <p>
   *                   Requires Assistant level permissions
   * 
   * @return  A JSON string containing all inscriptions
   */
  @GetMapping(path="/inscriptions")
  public ResponseEntity<String> getInscriptions(@RequestHeader("Authorization") String header) {
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {
      return new ResponseEntity<>(new LinkedMultiValueMap<>(), HttpStatus.NO_CONTENT);
    }
    if(!permissionService.validRole(token, Permissions.Assistant)) {
      return new ResponseEntity<>(new LinkedMultiValueMap<>(), HttpStatus.NO_CONTENT);
    }
    return ResponseEntity.ok(StringUtil.toString(inscriptionService.getAllInscriptions()));
  }

  /**
   *  Returns a timeline of slots for a given room on a given day
   * 
   * @param room_id   The id of the room in question
   * @param date      The date in question
   * @param header    A Bearer Token containing an authentication token (Authorization: Bearer {token}) Token contains {@value PermissionService#TOKEN_LENGTH} upper or lower case letters, or numbers <p>
   *                  Requires Student level permissions 
   * 
   * @return  An Iterable containing slots in a given room for a given day
   */
  @GetMapping(path="/timeline/{room_id}/{date}")
  public ResponseEntity<Iterable<UUID>> getTimeline(@PathVariable UUID room_id, @PathVariable LocalDate date, @RequestHeader("Authorization") String header) {
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {     //No need to check for role since an existing header means meeting permission requirements
      return new ResponseEntity<>(new LinkedMultiValueMap<>(), HttpStatus.NO_CONTENT);
    }
    Iterable<Booking> bookings = bookingService.findAllBookingsByRoomByDay(room_id, date);
    LocalTime start = roomService.findStartByRoomId(room_id);
    LocalTime end = roomService.findEndByRoomId(room_id);
    return ResponseEntity.ok(TimeUtil.getTimeline(bookings, start, end));
  }

}
