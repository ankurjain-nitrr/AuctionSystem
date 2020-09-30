package security;

import model.User;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import service.UserService;
import utils.Constants;

import javax.inject.Inject;
import java.util.Optional;

public class Secured extends Security.Authenticator {

    @Inject
    UserService userService;

    @Override
    public Optional<String> getUsername(Http.Request req) {
        Optional<String> token = req.getHeaders().get(Constants.HEADER_TOKEN);
        Optional<String> userId = req.getHeaders().get(Constants.HEADER_KEY_USER_ID);
        if (token.isPresent() && userId.isPresent()) {
            Optional<User> user = userService.authenticateToken(token.get(), userId.get());
            return user.map(User::getId);
        }
        return Optional.empty();
    }

    @Override
    public Result onUnauthorized(Http.Request req) {
        return unauthorized("You are not authorized to perform this action");
    }
}

