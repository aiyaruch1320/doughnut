package com.odde.doughnut.controllers;

import com.odde.doughnut.entities.User;
import com.odde.doughnut.entities.repositories.NoteRepository;
import com.odde.doughnut.services.ModelFactoryService;
import com.odde.doughnut.testability.DBCleaner;
import com.odde.doughnut.testability.MakeMe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:repository.xml"})
@ExtendWith(DBCleaner.class)
class IndexControllerTests {

  @Autowired private ModelFactoryService modelFactoryService;
  @Mock Model model;
  private IndexController controller;
  private MakeMe makeMe;

  @BeforeEach
  void setupController() {
    makeMe = new MakeMe();
  }

  @Test
  void visitWithNoUserSession() {
    controller = new IndexController(new TestCurrentUser(null), modelFactoryService);
    assertEquals("ask_to_login", controller.home(null, model));
  }

  @Test
  void visitWithUserSessionButNoSuchARegisteredUserYet() {
    controller = new IndexController(new TestCurrentUser(null), modelFactoryService);
    Principal principal = (UserPrincipal) () -> "1234567";
    assertEquals("register", controller.home(principal, model));
  }

  @Test
  void visitWithUserSessionAndTheUserExists() {
    User user = makeMe.aUser().please(modelFactoryService);
    Principal principal = (UserPrincipal) user::getExternalIdentifier;
    controller = new IndexController(new TestCurrentUser(user), modelFactoryService);
    assertEquals("index", controller.home(principal, model));
  }
}
