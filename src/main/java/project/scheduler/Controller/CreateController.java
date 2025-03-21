package project.scheduler.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.inject.Inject;
import project.scheduler.Repositories.CourseRepository;
import project.scheduler.Repositories.TokenRepository;
import project.scheduler.Repositories.UserRepository;
import project.scheduler.Tables.Course;
import project.scheduler.Tables.Token;
import project.scheduler.Tables.User;
import project.scheduler.Util.Password;


@Controller // This means that this class is a Controller
@RequestMapping(path="/add") // This means URL's start with /add (after Application path)
public class CreateController {
  @Autowired // This means to get the bean called userRepository
         // Which is auto-generated by Spring, we will use it to handle the data
  private UserRepository userRepository;
  @Inject
  private CourseRepository courseRepository;
  /*
  private RoomRepository roomRepository;
  private UserCourseRepository userCourseRepository;
  private RoomCourseRepository roomCourseRepository; */
  @Inject
  private TokenRepository tokenRepository;

  @PostMapping(path="/user") // Map ONLY POST Requests
  public @ResponseBody String addNewUser (@RequestParam Integer role, @RequestParam String name
      , @RequestParam String password, @RequestParam String email) {
    // @ResponseBody means the returned String is the response, not a view name
    // @RequestParam means it is a parameter from the GET or POST request
    Password pw = new Password(password);

    User new_user = userRepository.save(new User(role, name, pw.getPassword(), email));
    return String.format("Added user: %s with user id %d%n", name, new_user.getId());
  }

  @PostMapping(path="/token") // Map ONLY POST Requests
  public @ResponseBody String addNewWeeklyToken (@RequestParam Integer userId, @RequestParam String token) {
    // @ResponseBody means the returned String is the response, not a view name
    // @RequestParam means it is a parameter from the GET or POST request
    User u = userRepository.findById(userId).orElse(null);
    if(u == null) {
        return String.format("User ID not found%n");
    }
    Token t = new Token(u , token);
    tokenRepository.save(t);
    return String.format("Added token: %s for user %d%n", token, userId);
  }

  @PostMapping(path="/course") // Map ONLY POST Requests
  public @ResponseBody String addCourse (@RequestParam String name) {
    // @ResponseBody means the returned String is the response, not a view name
    // @RequestParam means it is a parameter from the GET or POST request
    Course c = new Course(name);
    c = courseRepository.save(c);
    return String.format("Added course '%s' with id: %d", name, c.getId());
  }

  @PostMapping(path="/student")
  public String inscribeStudent(@RequestBody Integer user_id, @RequestBody Integer course_id) {
      //TODO: process POST request
      
      return String.format("Inscribed user: %d into course %d", user_id, course_id);
  }
}
