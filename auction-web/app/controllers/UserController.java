package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import exception.AlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import model.DisplayUser;
import model.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.UserService;

import javax.inject.Inject;
import java.util.Objects;

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
        return badRequest("User with email already exists - " + user);
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
