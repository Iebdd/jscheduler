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
import project.scheduler.Util.UserToken;

@Service
public class MockDataService {          //Automatically inserts mock data on startup
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

    private static final String[] times = new String[] {
        "2025-04-16T15:00:00Z", "2025-04-16T17:00:00Z"
    };
    

    public void init() {
        addAdmin(admin);
    }

    private void addAdmin(User admin) {
        UserToken token = processResponse(addUser(admin.getRole().toString(), admin.getFirstName(), admin.getLastName(), admin.getPassword(), admin.getEmail()));
        if(token != null) {
            addCourses(token.getFirstToken());
            addRooms(token.getFirstToken());
        }
        addUsers();
        addBookings();
    }

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

    private void addBookings() {
        
    }

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

    private void addCourses(String token) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for(Course course : courses) {
            map.add("courseName", course.getName());
            map.add("token", token);
            sendPost(URI + "/add/course", map, Course.class);
            map.clear();
            courses_obj.add(sendGet(URI + "/read/courseName/" + course.getName(), String.class).getBody());
        }
    }

     private void addRooms(String token) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for(Room room : rooms) {
            map.add("roomName", room.getRoomName());
            map.add("token", token);
            sendPost(URI + "/add/room", map, Room.class);
            map.clear();
            rooms_obj.add(sendGet(URI + "/read/roomName/" + room.getRoomName(), String.class).getBody());
        }
    }


    private ResponseEntity<String> inscribe(String userId, String courseId, String token) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("user_id", userId);
        map.add("course_id", courseId);
        map.add("token", token);
        return sendPost(URI + "/add/inscription", map, String.class);
    }

    private <T> ResponseEntity<T> sendGet(String uri, Class<T> response_type) {
        return restTemplate.getForEntity(uri, response_type);
    }

    private <T> ResponseEntity<T> sendPost(String uri, MultiValueMap<String, String> map, Class<T> response_type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        return restTemplate.postForEntity(uri, request, response_type);
    }

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
