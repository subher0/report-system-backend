package com.texnoprom.controllers.acp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.texnoprom.controllers.acp.requests.ACPInfoRequest;
import com.texnoprom.database.entities.MainACPProperties;
import com.texnoprom.database.entities.User;
import com.texnoprom.database.exceptions.ErrorMessages;
import com.texnoprom.services.ACPService;
import com.texnoprom.services.SessionService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/acp")
public class ACPController {
  private final SessionService sessionService;
  private final ACPService acpService;

  @Autowired
  public ACPController(SessionService sessionService, ACPService acpService) {
    this.sessionService = sessionService;
    this.acpService = acpService;
  }

  @RequestMapping(path = "/{sessionid}/{qrid}", method = RequestMethod.GET)
  public ResponseEntity getACPInfo(@PathVariable(name = "sessionid") String sessionId,
      @PathVariable(name = "qrid") Long id) {
    final User user = sessionService.getUserBySessionId(sessionId);

    if (user != null) {
      MainACPProperties properties = acpService.getACPPropertiesById(id);
      if (properties != null) {
        return ResponseEntity.ok().body(properties.toJSON());
      } else {
        return ResponseEntity.badRequest().body(ACPErrors.getErrorMessage(ACPErrors.BAD));
      }
    }

    final Long size = sessionService.getLength();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(ACPErrors.getErrorMessage(ACPErrors.ACCESS_DENIED));
  }
}
