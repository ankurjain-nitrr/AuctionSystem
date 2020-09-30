package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import exception.AlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import model.AuthenticationToken;
import model.DisplayUser;
import model.User;
import org.json.JSONObject;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.UserService;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class UserController extends Controller {

    @Inject
    UserService userService;

    public Result create(Http.Request req) {
        JsonNode jsonNode = req.body().asJson();
        User user = Json.fromJson(jsonNode, User.class);
        Objects.requireNonNull(user);
        try {
            userService.create(user);
            return ok(Json.toJson(new DisplayUser(user)));
        } catch (AlreadyExistsException e) {
            log.error("Error creating user - " + user);
        }
        return badRequest("User with email already exists - " + user.getEmail());
    }

    public Result authenticate(Http.Request req) {
        JsonNode jsonNode = req.body().asJson();
        String email = jsonNode.get("email").asText();
        String passwdmd5 = jsonNode.get("passwd").asText();
        Objects.requireNonNull(email);
        Objects.requireNonNull(passwdmd5);
        Optional<AuthenticationToken> authenticationToken =
                userService.authenticateAndGenerateToken(email, passwdmd5);
        if (authenticationToken.isPresent()) {
            JSONObject tokenResponse = new JSONObject();
            tokenResponse.put("token", authenticationToken.get().getToken());
            tokenResponse.put("expiry", authenticationToken.get().getExpiry());
            tokenResponse.put("userId", authenticationToken.get().getUserId());
            return ok(tokenResponse.toString());
        }
        return unauthorized();
    }

    public Result get(String uid) {
        Objects.requireNonNull(uid);
        User user = userService.get(uid);
        if (user != null) {
            return ok(Json.toJson(new DisplayUser(user)));
        }
        return notFound("User with id not found" + uid);
    }

}
