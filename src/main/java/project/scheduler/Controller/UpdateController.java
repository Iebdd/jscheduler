package project.scheduler.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import project.scheduler.Repositories.UserRepository;
import project.scheduler.Tables.User;
import project.scheduler.Util.Password;


@Controller
@RequestMapping(path="/update")
public class UpdateController {
  @Autowired
  private UserRepository userRepository;
/*   private CourseRepository courseRepository;
  private RoomRepository roomRepository;
  private UserCourseRepository userCourseRepository;
  private RoomCourseRepository roomCourseRepository;
  private TokenRepository tokenRepository; */

  @PostMapping(path="/password")
  public @ResponseBody String setPasswordById(@RequestParam String old_password, @RequestParam String new_password, 
    @RequestParam Integer user_id) {
      User new_user = userRepository.findById(user_id).orElse(null);
      if (new_user == null || !Password.compare(old_password, new_user.getPassword())) {
        return String.format("User %d not found or password incorrect%n", user_id);
      }
      Password new_pw = new Password(new_password);
      userRepository.updatePassword(new_pw.getPassword(), user_id);
      return String.format("Saved new password for user: %d%n", user_id);
  }
  
}
