package project.scheduler.Controller;

import java.time.Instant;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.inject.Inject;
import project.scheduler.Repositories.TokenRepository;
import project.scheduler.Repositories.UserRepository;
import project.scheduler.Tables.Token;
import project.scheduler.Tables.User;
import project.scheduler.Util.Password;
import project.scheduler.Util.SingleToken;


@Controller
@RequestMapping(path="/verify")
public class VerifyController {
  @Autowired
  private UserRepository userRepository;
/*   private CourseRepository courseRepository;
  private RoomRepository roomRepository;
  private UserCourseRepository userCourseRepository;
  private RoomCourseRepository roomCourseRepository; */
  @Inject
  private TokenRepository tokenRepository;

  @PostMapping(path="/password")
  public @ResponseBody Boolean verifyPassword(@RequestParam String check_password, @RequestParam String email) {
    return Password.compare(check_password, userRepository.findPasswordByEmail(email));
  }

  @PostMapping(path="/login")
  public @ResponseBody SingleToken login(@RequestParam String email, @RequestParam String password) {
    User user = userRepository.findUserByEmail(email);
    boolean pw_match = Password.compare(password, user.getPassword());
    String[] tokens;
    if(pw_match) {
      tokenRepository.deleteExpired(Instant.now());
      tokens = tokenRepository.findTokenByUser(user.getId());
      if(tokens.length == 0) {
        RandomStringUtils tok_gen = RandomStringUtils.secure();
        SingleToken new_token =  new SingleToken(tok_gen.next(25, true, true), false);
        tokenRepository.save(new Token(user, new_token.getTokens()[0], Instant.now().plusSeconds(604800)));
        return new_token;
      }
      tokenRepository.refreshToken(Instant.now().plusSeconds(604800), user.getId());
      return new SingleToken(tokens, true);
    }
    return null;
  }
}
