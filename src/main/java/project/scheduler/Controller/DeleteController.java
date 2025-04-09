package project.scheduler.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import project.scheduler.Repositories.TokenRepository;


@Controller
@RequestMapping(path="/delete")
public class DeleteController {
  @Autowired
  private TokenRepository tokenRepository;

  /*
  private RoomRepository roomRepository;
  private UserCourseRepository userCourseRepository;
  private RoomCourseRepository roomCourseRepository; */
}
