package com.texnoprom.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.texnoprom.controllers.requests.AuthenticationRequest;
import com.texnoprom.controllers.requests.RegistrationRequest;
import com.texnoprom.controllers.requests.SessionId;
import com.texnoprom.database.entities.User;
import com.texnoprom.services.AccountService;
import com.texnoprom.services.SessionService;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"*"})
@RestController
public class RegistrationController {
  private final AccountService accountService;
  private final SessionService sessionService;

  @Autowired
  public RegistrationController(AccountService accountService, SessionService sessionService) {
    this.accountService = accountService;
    this.sessionService = sessionService;
  }

  @RequestMapping(path = "/api/session", method = RequestMethod.GET)
  public ResponseEntity isAuthenticated(HttpSession httpSession) {
    final String sessionId = httpSession.getId();
    final User user = sessionService.getUserBySessionId(sessionId);

    if (user != null) {
      final ObjectMapper mapper = new ObjectMapper();
      final ObjectNode response = mapper.createObjectNode();

      response.put("sessionId", user.getUserId());

      return ResponseEntity.ok().body(response);
    }

    final Long size = sessionService.getLength();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(httpSession.getId());
  }

  @RequestMapping(path = "/api/session", method = RequestMethod.POST)
  public ResponseEntity auth(@RequestBody AuthenticationRequest body) {

    final String login = body.getName();
    final String password = body.getPassword();
    if (StringUtils.isEmpty(login)
        || StringUtils.isEmpty(password)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RegistrationErrors.getErrorMessage(
          RegistrationErrors.EMPTY_FIELDS));
    }

    final User user = accountService.getUser(login);

    if (user == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RegistrationErrors.getErrorMessage(
          RegistrationErrors.WRONG_LOGIN_PASSWORD));
    }

    if (user.matchPassword(password)) {
      String hash = sessionService.addUserToSession(user);

      final ObjectMapper mapper = new ObjectMapper();
      final ObjectNode response = mapper.createObjectNode();

      response.put("sessionId", hash);

      return ResponseEntity.ok(response);
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RegistrationErrors.getErrorMessage(
        RegistrationErrors.WRONG_LOGIN_PASSWORD));
  }

  @RequestMapping(path = "/api/session", method = RequestMethod.DELETE)
  public ResponseEntity logout(@RequestBody SessionId body) {

    final String sessionId = body.getSessionId();
    final User user = sessionService.getUserBySessionId(sessionId);

    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RegistrationErrors.getErrorMessage(
          RegistrationErrors.ACCESS_DENIED));
    }

    sessionService.endSession(sessionId);

    return ResponseEntity.ok("{}");
  }

  @RequestMapping(path = "/api/user", method = RequestMethod.POST)
  public ResponseEntity register(@RequestBody RegistrationRequest body) {

    final String name = body.getName();
    final String password = body.getPassword();

    if (StringUtils.isEmpty(name)
        || StringUtils.isEmpty(password)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RegistrationErrors.getErrorMessage(
          RegistrationErrors.EMPTY_FIELDS));
    }

    User user = new User(name, password);

    user = accountService.addUser(user);

    if (user == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RegistrationErrors
          .getErrorMessage(RegistrationErrors.EXISTING_USER));
    }

    String hash = sessionService.addUserToSession(user);
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode response = mapper.createObjectNode();
    response.put("sessionId", hash);

    return ResponseEntity.ok(response);
  }

  @RequestMapping(path = "api/user/{login}", method = RequestMethod.GET)
  public ResponseEntity getUser(@PathVariable("login") String login, @RequestBody SessionId body) {
    final User user = accountService.getUser(login);

    if (user == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RegistrationErrors.getErrorMessage(
          RegistrationErrors.WRONG_ID));
    }

    return ResponseEntity.ok(user.toJSON());
  }

  @RequestMapping(path = "api/user/session/{session}", method = RequestMethod.GET)
  public ResponseEntity getUserBySessionId(@PathVariable("session") String sessionId) {
    final User user = sessionService.getUserBySessionId(sessionId);

    if (user == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RegistrationErrors.getErrorMessage(
          RegistrationErrors.WRONG_ID));
    }

    return ResponseEntity.ok(user.toJSON());
  }


  @RequestMapping(path = "api/user/{id}", method = RequestMethod.PUT)
  public ResponseEntity editUser(@PathVariable("id") int id,
      @RequestBody RegistrationRequest body,
      HttpSession httpSession) {

    final User user = accountService.getUser(id);

    if (user == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RegistrationErrors.getErrorMessage(
          RegistrationErrors.WRONG_ID));
    }

    final User currentUser = sessionService.getUserBySessionId(httpSession.getId());

    if (currentUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RegistrationErrors.getErrorMessage(
          RegistrationErrors.ACCESS_DENIED));
    }

    if (currentUser.equals(user)) {
      if (body.getName() != null) {
        user.setName(body.getName());
      }
      if (body.getPassword() != null) {
        user.setPassword(body.getPassword());
      }

      accountService.updateUser(user);
      return ResponseEntity.ok(user.toJSON());
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RegistrationErrors.getErrorMessage(
        RegistrationErrors.ACCESS_DENIED));
  }

  @RequestMapping(path = "api/user/{id}", method = RequestMethod.DELETE)
  public ResponseEntity deleteUser(@PathVariable("id") int id, HttpSession httpSession) {

    final User user = accountService.getUser(id);

    if (user == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RegistrationErrors.getErrorMessage(
          RegistrationErrors.WRONG_ID));
    }

    final User existingUser = sessionService.getUserBySessionId(httpSession.getId());

    if (!existingUser.equals(user)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RegistrationErrors.getErrorMessage(
          RegistrationErrors.ACCESS_DENIED));
    }

    accountService.deleteUser(user);
    return ResponseEntity.ok("{}");
  }

  @RequestMapping(path = "/api/userlist", method = RequestMethod.GET)
  public ResponseEntity getUserList(HttpSession httpSession) {
    final String sessionId = httpSession.getId();

    final ObjectMapper mapper = new ObjectMapper();

    final ArrayNode userJsonList = mapper.createArrayNode();

    final ArrayList<User> userList = (ArrayList<User>) accountService.getAllUsers();

    for (User user : userList) {
      final ObjectNode entry = mapper.createObjectNode();
      userJsonList.add(entry);
    }

    try {
      return ResponseEntity.ok().body(mapper.writeValueAsString(userJsonList));
    } catch (JsonProcessingException ex) {
      return ResponseEntity.ok().body("{}");
    }
  }
}
