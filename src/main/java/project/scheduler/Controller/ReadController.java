package project.scheduler.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import project.scheduler.Services.PermissionService;
import project.scheduler.Services.PermissionService.Permissions;
import project.scheduler.Services.RoomService;
import project.scheduler.Tables.Booking;
import project.scheduler.Tables.Course;
import project.scheduler.Tables.Room;


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
   * @param room_id The id of the room in question
   * 
   * @return  An Iterable of all Bookings within the room
   */
  @GetMapping(path="/roomBookings/{room_id}")
  public ResponseEntity<Iterable<Booking>> getBookedCourses(@PathVariable UUID room_id) {
    return bookingService.findAllBookingsByRoom(room_id);
  }

  /**
   *  Returns all course taking place in a given room on a given day
   * 
   * @param room_id The id of the room in question
   * @param date    The day in question
   * 
   * @return  An Iterable of all Bookings within the room on that day
   */
  @GetMapping(path="/roomBookings/{room_id}/{date}")
  public ResponseEntity<Iterable<Booking>> getBookedCoursesByDay(@PathVariable UUID room_id, @PathVariable LocalDate date) {
    LocalDateTime start_day = LocalDateTime.of(date, roomService.findStartByRoomId(room_id));
    LocalDateTime end_day = LocalDateTime.of(date, roomService.findEndByRoomId(room_id));
    System.out.println(start_day);
    return bookingService.findAllBookingsByRoomByDay(room_id, start_day, end_day);
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
  public ResponseEntity<Iterable<Booking>> getInscribedCourses(@PathVariable UUID user_id, @RequestHeader("Authorization") String header) {
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {
      return new ResponseEntity<>(new LinkedMultiValueMap<>(), HttpStatus.UNAUTHORIZED);
    }
    if(!permissionService.validRole(token, user_id, Permissions.Admin)) {
      return new ResponseEntity<>(new LinkedMultiValueMap<>(), HttpStatus.UNAUTHORIZED);
    }
    return bookingService.findAllBookingsByUser(user_id);
  }

  @GetMapping(path="/bookings")
  public ResponseEntity<Iterable<Booking>> getBookings(@RequestHeader("Authorization") String header) {
    String token = permissionService.validAuthHeader(header);
    if(token.length() == 0) {
      return new ResponseEntity<>(new LinkedMultiValueMap<>(), HttpStatus.UNAUTHORIZED);
    }
    if(!permissionService.validRole(token, Permissions.Assistant)) {
      return new ResponseEntity<>(new LinkedMultiValueMap<>(), HttpStatus.UNAUTHORIZED);
    }
    return bookingService.getAllBookings();
  }

}
