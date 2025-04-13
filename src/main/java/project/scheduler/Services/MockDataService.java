package project.scheduler.Services;



import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;

import project.scheduler.Tables.Course;
import project.scheduler.Tables.Room;
import project.scheduler.Tables.User;
import project.scheduler.Util.UserToken;
import reactor.core.publisher.Mono;

@Service
public class MockDataService {          //Automatically inserts mock data on startup
    private final WebClient client = WebClient.builder()
        .baseUrl(URI)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    private static final String URI = "http://localhost:8080/";

    private static final User admin = new User(2, "Herbert", "Horowitz", "12345", "admin@scheduler.com");
    private static final User assistant = new User(1, "Linda", "Lindisfarne", "password", "assistant@scheduler.com");
    private static final User student1 = new User(0, "Hans", "Heimer", "67890", "student1@scheduler.com");
    private static final User student2 = new User(0, "Brenda", "McDonald", "!aOssU78D2", "student2@scheduler.com");
    private static final User student3 = new User(0, "Armaud", "Pernault", "baguette", "honhon@scheduler.com");

    private static final Course[] courses = new Course[] {
        new Course("Mathematics 1"),
        new Course("Algebra 101"),
        new Course("English Pop Culture")
    };

    private static Room[] rooms_obj;
    private static Course[] course_obj;

    private static final Room[] rooms = new Room[] {
        new Room("CZ 001"),
        new Room("CZ 002"),
        new Room("CZ 003")
    };
    

    public void init() {
        addAdmin(admin)
            .doOnNext(value -> addUsers());
            
    }

    private Mono<String> addAdmin(User admin) {
        addUser(admin.getRole().toString(), admin.getFirstName(), admin.getLastName(), admin.getPassword(), admin.getEmail())
                .subscribe(
                    value -> addCourses(value.getFirstToken())
                                .then(addRooms(value.getFirstToken()))
                                .then(addUser(student3.getRole().toString(), student3.getFirstName(), student3.getLastName(), student3.getPassword(), student3.getEmail())),
                    error -> System.out.println(error.toString()));
        System.out.println("Success");
        return Mono.just("Infrastructure initialised");
    }

    private Mono<String> addUsers() {
        addUser(student1.getRole().toString(), student1.getFirstName(), student1.getLastName(), student1.getPassword(), student1.getEmail())
                .subscribe(
                    value -> {
                        inscribe(value.getUserId(), course_obj[0].getId(), value.getFirstToken());
                        inscribe(value.getUserId(), course_obj[2].getId(), value.getFirstToken());
                    },
                    error -> System.out.println("Student 1 :" + error.toString())
                );
        addUser(student2.getRole().toString(), student2.getFirstName(), student2.getLastName(), student2.getPassword(), student2.getEmail())
                .subscribe(
                    value -> {
                        inscribe(value.getUserId(), course_obj[2].getId(), value.getFirstToken());
                        inscribe(value.getUserId(), course_obj[1].getId(), value.getFirstToken());
                    },
                    error -> System.out.println("Student 2 :" + error.toString())
                );
        addUser(assistant.getRole().toString(), assistant.getFirstName(), assistant.getLastName(), assistant.getPassword(), assistant.getEmail())
                .subscribe(
                    value -> {
                        inscribe(value.getUserId(), course_obj[1].getId(), value.getFirstToken());
                        inscribe(value.getUserId(), course_obj[2].getId(), value.getFirstToken());
                    },
                    error -> System.out.println("Assistant :" + error.toString())
                );
        return Mono.just("Users and inscriptions initialised");
    }

    private Mono<String> addCourses(String token) {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (Course course: courses) {
            map.add("courseName", course.getName());
            map.add("token", token);
            sendStringRequest(setHeader(HttpMethod.POST, "add/course", map))
                .subscribe(
                    value -> System.out.println("Inserted"),
                    error -> System.out.println("Error in courses: " + error.toString())
                );
            map.clear();
        }
        sendCourseRequest(setHeader(HttpMethod.GET, "read/courses", map))
            .subscribe(
                value -> course_obj = value,
                error -> System.out.println("Unable to read courses: " + error.toString())
            );
        return Mono.just("Courses initialised");
    }

    private Mono<String> inscribe(UUID userId, UUID courseId, String token) {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userId", userId.toString());
        map.add("courseId", String.valueOf(courseId));
        map.add("token", token);
        sendStringRequest(setHeader(HttpMethod.POST, "add/inscription", map))
        .subscribe(
            value -> System.out.println("Inscribed"),
            error -> System.out.println("Error in inscription: " + error.toString())
        );
        map.clear();
        return Mono.just("Inscribed");
    }

    private Mono<String> addRooms(String token) {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (Room room: rooms) {
            map.add("roomName", room.getName());
            map.add("token", token);
            sendStringRequest(setHeader(HttpMethod.POST, "add/room", map))
            .subscribe(
                value -> System.out.println("Inserted"),
                error -> System.out.println("Error: " + error.toString())
            );
            map.clear();
        }
        sendRoomRequest(setHeader(HttpMethod.GET, "read/rooms", map))
            .subscribe(
                value -> rooms_obj = value,
                error -> System.out.println("Unable to read the room: " + error.toString())
            );
        return Mono.just("Room initialised");
    }

    private RequestHeadersSpec<?> setHeader(HttpMethod request_type, String uri, LinkedMultiValueMap<String, String> values) {
        UriSpec<RequestBodySpec> uriSpec = client.method(request_type);
        RequestBodySpec bodySpec = uriSpec.uri(uri); 
        return bodySpec.body(BodyInserters.fromMultipartData(values));
    }

    private Mono<Object> sendRequest(RequestHeadersSpec<?> headersSpec) {
        return headersSpec.exchangeToMono(inner_response -> {
            if (inner_response.statusCode().equals(HttpStatus.OK)) {
                return inner_response.bodyToMono(Object.class);
            } else if (inner_response.statusCode().is4xxClientError()) {
                return inner_response.createException()
                                    .flatMap(Mono::error);
            } else {
                return inner_response.createException()
                                    .flatMap(Mono::error);
            }
        });
    }
    
    private Mono<String> sendStringRequest(RequestHeadersSpec<?> headersSpec) {
        return headersSpec.exchangeToMono(inner_response -> {
            if (inner_response.statusCode().equals(HttpStatus.OK)) {
                return inner_response.bodyToMono(String.class);
            } else if (inner_response.statusCode().is4xxClientError()) {
                return inner_response.bodyToMono(String.class);
            } else {
                return inner_response.createException()
                                    .flatMap(Mono::error);
            }
        });
    }

    private Mono<UserToken> sendUserTokenRequest(RequestHeadersSpec<?> headersSpec) {
        return headersSpec.exchangeToMono(inner_response -> {
            if (inner_response.statusCode().equals(HttpStatus.OK)) {
                return inner_response.bodyToMono(UserToken.class);
            } else if (inner_response.statusCode().is4xxClientError()) {
                return inner_response.createException()
                                    .flatMap(Mono::error);
            } else {
                return inner_response.createException()
                                    .flatMap(Mono::error);
            }
        });
    }

    private Mono<Room[]> sendRoomRequest(RequestHeadersSpec<?> headersSpec) {
        return headersSpec.exchangeToMono(inner_response -> {
            if (inner_response.statusCode().equals(HttpStatus.OK)) {
                return inner_response.bodyToMono(Room[].class);
            } else if (inner_response.statusCode().is4xxClientError()) {
                return inner_response.createException()
                                    .flatMap(Mono::error);
            } else {
                return inner_response.createException()
                                    .flatMap(Mono::error);
            }
        });
    }

    private Mono<Course[]> sendCourseRequest(RequestHeadersSpec<?> headersSpec) {
        return headersSpec.exchangeToMono(inner_response -> {
            if (inner_response.statusCode().equals(HttpStatus.OK)) {
                return inner_response.bodyToMono(Course[].class);
            } else if (inner_response.statusCode().is4xxClientError()) {
                return inner_response.createException()
                                    .flatMap(Mono::error);
            } else {
                return inner_response.createException()
                                    .flatMap(Mono::error);
            }
        });
    }

    private Mono<UserToken> addUser(String role, String firstName, String lastName, String password, String email) {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("role", role);
        map.add("lastName", lastName);
        map.add("firstName", firstName);
        map.add("password", password);
        map.add("email", email);                                
        return sendUserTokenRequest(setHeader(HttpMethod.POST, "add/user", map));
    }

}
