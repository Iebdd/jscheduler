package project.scheduler;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.inject.Inject;


@Controller // This means that this class is a Controller
@RequestMapping(path="/token") // This means URL's start with /token (after Application path)
public class TokenController {
  @Autowired // This means to get the bean called tokenRepository
         // Which is auto-generated by Spring, we will use it to handle the data
  private TokenRepository tokenRepository;
  @Inject
  private UserRepository userRepository;

  @PostMapping(path="/add") // Map ONLY POST Requests
  public @ResponseBody String addNewWeeklyToken (@RequestParam Integer userId, @RequestParam String token) {
    // @ResponseBody means the returned String is the response, not a view name
    // @RequestParam means it is a parameter from the GET or POST request
    User u = userRepository.findById(userId).orElse(null);
    if(u == null) {
        return "User ID not found";
    }
    Token t = new Token(u , token);
    tokenRepository.save(t);
    return String.format("Added token: %s for user %d", token, userId);
  }

  @PostMapping(path="/clean")
  public @ResponseBody String deleteExpired() {
    Integer[] count = tokenRepository.findExpired(Instant.now());
    tokenRepository.deleteByIdIn(count);
    return String.format("Deleted %d expired entries", count.length);
  }

  @GetMapping(path="/verify/{userId}/{token}")
  public @ResponseBody Boolean verifyToken(@PathVariable Integer userId, @PathVariable String token) {
    Integer id = tokenRepository.findToken(userId, token, Instant.now());
    if (id != null) {
      tokenRepository.refreshToken(Instant.now().plusSeconds(604800), id);
      return true;
    }
    return false;
  }
  
  @GetMapping(path="/all")
  public @ResponseBody Iterable<Token> getAllTokens() {
    // This returns a JSON or XML with the users
    return tokenRepository.findAll();
  }
}
