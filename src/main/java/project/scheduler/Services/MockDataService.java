package project.scheduler.Services;



import java.util.ArrayList;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import project.scheduler.Tables.Course;
import project.scheduler.Tables.Room;
import project.scheduler.Tables.User;
import project.scheduler.Util.UserBooking;
import project.scheduler.Util.UserToken;

/**
 * Service that automatically inserts mock data on startup, if not present
 */
@Service
public class MockDataService {          
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String URI = "http://localhost:8080";

    private static final User admin = new User(2, "Herbert", "Horowitz", "12345", "admin@scheduler.com");
    private static final User assistant = new User(1, "Linda", "Lindisfarne", "password", "assistant@scheduler.com");
    private static final User student1 = new User(0, "Hans", "Heimer", "67890", "student1@scheduler.com");
    private static final User student2 = new User(0, "Brenda", "McDonald", "!aOssU78D2", "student2@scheduler.com");
    private static final User student3 = new User(0, "Armaud", "Pernault", "baguette", "student3@scheduler.com");


    private static final Course[] courses = new Course[] {
        new Course("Mathematics 1"),
        new Course("Algebra 101"),
        new Course("English Pop Culture")
    };
    private static final ArrayList<String> rooms_obj = new ArrayList<>();
    private static final ArrayList<String> courses_obj = new ArrayList<>();

    private static final Room[] rooms = new Room[] {
        new Room("CZ 001"),
        new Room("CZ 002"),
        new Room("CZ 003")
    };

    private static final String[][] times = new String[][] {
        {"2025-04-25T15:05:00", "2025-04-25T17:00:00"},
        {"2025-04-25T18:12:00", "2025-04-25T19:45:00"},
        {"2025-04-25T12:44:00", "2025-04-25T15:15:00"}
    };
    

    /**
     * The initial entrypoint of the Service
     */
    public void init() {
        if(isAdminPresent()) {
            System.out.println("Mock Data present");
        } else {
            addAdmin(admin);
        }
    }

    /**
     * Checks if the data is already present by queriying the database for the admin user
     * 
     * @return  True if the admin user is present, false if not
     */
    private boolean isAdminPresent() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", admin.getEmail());
        map.add("password", admin.getPassword());
        ResponseEntity<UserToken> admin_response = sendPost(URI + "/verify/login", map, UserToken.class);
        return (admin_response.getBody() != null);
    }

    /**
     * Adds the admin user and uses the returned token to create the courses and rooms as well as bookings
     * 
     * @param admin A User object representing the admin user
     */
    private void addAdmin(User admin) {
        UserToken token = processResponse(addUser(admin.getRole().toString(), admin.getFirstName(), admin.getLastName(), admin.getPassword(), admin.getEmail()));
        if(token != null) {
            addCourses(token.getFirstToken());
            addRooms(token.getFirstToken());
            addUsers();
            addBookings(token.getFirstToken());
        } else {
            System.out.println("Admin trouble!");
        }
    }

    /**
     *  Processes an HTTP request and returns it if it has returned 200 or prints the error if it wasn't
     * 
     * @param <T>       A generic object representing the return value
     * @param response  The response to be analysed
     * 
     * @return  The body of the response if the code was 200 or null if it wasn't
     */
    private <T> T processResponse(ResponseEntity<T> response) {
        switch(response.getStatusCode()) {
            case HttpStatus.OK -> {
                return response.getBody();
            }
            default -> {
                System.out.printf("%s: %s", response.getStatusCode(), response.getBody());
                return null;
            }

        }
    }

    /**
     * Adds bookings into the database
     * 
     * @param token The admin token to validate the requests
     */
    private void addBookings(String token) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for(int outer_i = 0; outer_i < courses.length; outer_i++) {
            map.add("course_id", courses_obj.get(outer_i));
            map.add("room_id", rooms_obj.get(outer_i));
            map.add("start", times[outer_i][0]);
            map.add("end", times[outer_i][1]);
            sendPost(URI + "/add/booking", token, map, UserBooking.class);
            map.clear();
        }
    }

    /**
     * Adds an assistant and three student accounts and inscribes them
     */
    private void addUsers() {
        UserToken student1_t = processResponse(addUser(student1.getRole().toString(), student1.getFirstName(), student1.getLastName(), student1.getPassword(), student1.getEmail()));
        if(student1_t != null) {
            inscribe(student1_t.getUserId().toString(), courses_obj.get(0), student1_t.getFirstToken());
            inscribe(student1_t.getUserId().toString(), courses_obj.get(1), student1_t.getFirstToken());
        }
        UserToken student2_t = processResponse(addUser(student2.getRole().toString(), student2.getFirstName(), student2.getLastName(), student2.getPassword(), student2.getEmail()));
        if(student2_t != null) {
            inscribe(student2_t.getUserId().toString(), courses_obj.get(0), student2_t.getFirstToken());
            inscribe(student2_t.getUserId().toString(), courses_obj.get(2), student2_t.getFirstToken());
        }
        UserToken student3_t = processResponse(addUser(student3.getRole().toString(), student3.getFirstName(), student3.getLastName(), student3.getPassword(), student3.getEmail()));
        UserToken assistant_t = processResponse(addUser(assistant.getRole().toString(), assistant.getFirstName(), assistant.getLastName(), assistant.getPassword(), assistant.getEmail()));
        if(student3_t != null && assistant_t != null) {
            inscribe(student3_t.getUserId().toString(), courses_obj.get(1), assistant_t.getFirstToken());
            inscribe(student3_t.getUserId().toString(), courses_obj.get(2), assistant_t.getFirstToken());
        }
    }

    /**
     * Adds courses into the database
     * 
     * @param token The admin token to validate the requests
     */
    private void addCourses(String token) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for(Course course : courses) {
            map.add("courseName", course.getCourseName());
            map.add("token", token);
            sendPost(URI + "/add/course", token, map, Course.class);
            map.clear();
            courses_obj.add(sendGet(URI + "/read/course/" + course.getCourseName(), String.class));
        }
    }

    /**
     * Adds rooms into the database
     * 
     * @param token The admin token to validate the requests
     */
     private void addRooms(String token) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for(Room room : rooms) {
            map.add("roomName", room.getRoomName());
            sendPost(URI + "/add/room", token, map, Room.class);
            map.clear();
            rooms_obj.add(sendGet(URI + "/read/room/" + room.getRoomName(), String.class));
        }
    }


    /**
     *  Inscribes students into courses
     * 
     * @param userId    The id of the student as a String - Must be in UUID format of (DDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD)
     * @param courseId  The id of the course as a String - Must be in UUID format of (DDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD)
     * @param token     The token to validate the request
     * 
     * @return  A String containing feedback on the operation (Refer to response codes for response handling)
     */
    private ResponseEntity<String> inscribe(String userId, String courseId, String token) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("user_id", userId);
        map.add("course_id", courseId);
        map.add("token", token);
        return sendPost(URI + "/add/inscription", token, map, String.class);
    }

    /**
     * Sends a GET request to the given URI
     * 
     * @param <T>           The object type of the response
     * @param uri           The URL for the request to be sent to
     * @param response_type The class of the object type of the response
     * 
     * @return  The body of the response
     */
    private <T> T sendGet(String uri, Class<T> response_type) {
        return restTemplate.getForObject(uri, response_type);
    }

    /**
     * Sends a POST request to the given URI without authentification
     * 
     * @param <T>           The object type of the response
     * @param uri           The URL for the request to be sent to
     * @param map           The body of the request as a Multi Value Map of Strings
     * @param response_type The class of the object type of the response
     * 
     * @return  The response from the request
     */
    private <T> ResponseEntity<T> sendPost(String uri, MultiValueMap<String, String> map, Class<T> response_type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        return restTemplate.postForEntity(uri, request, response_type);
    }

    /**
     *  Sends a POST request to the given URI with authentification
     * 
     * @param <T>   The object type of the response
     * @param uri   The URL for the request to be sent to
     * @param token The token to vlidate the request
     * @param map   The body of the request as a Multi Value Map of Strings
     * @param response_type The class of the object type of the response
     * 
     * @return  The response from the request
     */
    private <T> ResponseEntity<T> sendPost(String uri, String token, MultiValueMap<String, String> map, Class<T> response_type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        return restTemplate.postForEntity(uri, request, response_type);
    }

    /**
     * Adds a new user into the database
     * 
     * @param role      The role of the new user
     * @param firstName The first name of the new user
     * @param lastName  The last name of the new user
     * @param password  The password of the new user
     * @param email     The email of the new user
     * 
     * @return  A UserToken for the newly created user
     */
    private ResponseEntity<UserToken> addUser(String role, String firstName, String lastName, String password, String email) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("role", role);
        map.add("lastName", lastName);
        map.add("firstName", firstName);
        map.add("password", password);
        map.add("email", email);
        return sendPost(URI + "/add/user", map, UserToken.class);
    }

}
