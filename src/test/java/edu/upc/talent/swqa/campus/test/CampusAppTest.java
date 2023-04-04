package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.CampusApp;
import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.domain.UsersRepository;
import edu.upc.talent.swqa.campus.test.utils.CampusAppState;
import edu.upc.talent.swqa.campus.test.utils.Group;
import edu.upc.talent.swqa.campus.test.utils.InMemoryEmailSender;
import edu.upc.talent.swqa.campus.test.utils.InMemoryUsersRepository;
import edu.upc.talent.swqa.campus.test.utils.SentEmail;
import edu.upc.talent.swqa.campus.test.utils.UsersRepositoryState;
import static edu.upc.talent.swqa.test.utils.Asserts.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import java.util.Set;

public final class CampusAppTest {
  private final CampusAppState defaultInitialState = new CampusAppState(
        new UsersRepositoryState(
              Set.of(
                    new User("1", "John", "Doe", "john.doe@example.com", "student", "swqa"),
                    new User("2", "Jane", "Doe", "jane.doe@example.com", "student", "swqa"),
                    new User("3", "Mariah", "Harris", "mariah.hairam@example.com", "teacher", "swqa")
              ),
              Set.of(new Group(1, "swqa"))
        ),
        Set.of()
  );

  private CampusApp setInitialState(final CampusAppState initialState) {
    return new CampusApp(
          new InMemoryUsersRepository(initialState.usersRepositoryState()),
          new InMemoryEmailSender(initialState.sentEmails())
    );
  }

  @Test
  public void testCreateGroup(){
    final var state = new CampusAppState(
          new UsersRepositoryState(Set.of(), Set.of(new Group(1, "swqa"))),
          Set.of()
    ).copy();
    final var app = setInitialState(state);
    app.createGroup("bigdata");
    final var expectedFinalState = new CampusAppState(
          new UsersRepositoryState(
                Set.of(),
                Set.of(new Group(1, "swqa"), new Group(2, "bigdata"))
          ),
          Set.of()
    );
    assertEquals(expectedFinalState, state);
  }

  @Test
  public void testSendEmailToGroup() {
    final var state = defaultInitialState.copy();
    final var app = setInitialState(state);
    final var subject = "New campus!";
    final var body = "Hello everyone! We just created a new virtual campus!";
    app.sendMailToGroup("swqa", subject, body);
    final var expectedFinalState = new CampusAppState(
          defaultInitialState.usersRepositoryState(),
          Set.of(
                new SentEmail("john.doe@example.com", subject, body),
                new SentEmail("jane.doe@example.com", subject, body),
                new SentEmail("mariah.hairam@example.com", subject, body)
          )
    );
    assertEquals(expectedFinalState, state);
  }

  @Test
  public void testSendEmailToGroupRole() {
    final var state = defaultInitialState.copy();
    final var app = setInitialState(state);
    final var subject = "Hey! Teacher!";
    final var body = "Let them students alone!!";
    app.sendMailToGroupRole("swqa", "teacher", subject, body);
    final var expectedFinalState = new CampusAppState(
          defaultInitialState.usersRepositoryState(),
          Set.of(new SentEmail("mariah.hairam@example.com", subject, body))
    );
    assertEquals(expectedFinalState, state);
  }

  @Test
  void testCreateUser() {
    // Arrange
    String name = "John";
    String surname = "Doe";
    String email = "johndoe@example.com";
    String role = "student";
    String groupName = "mygroup";
    UsersRepository mockRepository = mock(UsersRepository.class);

    // Act
    mockRepository.createUser(name, surname, email, role, groupName);

    // Assert
    verify(mockRepository).createUser(name, surname, email, role, groupName);
  }



}
