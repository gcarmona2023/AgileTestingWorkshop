package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.CampusApp;
import edu.upc.talent.swqa.campus.domain.EmailSender;
import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.domain.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

public final class CampusAppMockedTest {

  final List<User> initialUsers = List.of(
        new User("1", "John", "Doe", "john.doe@example.com", "student", "swqa"),
        new User("2", "Jane", "Doe", "jane.doe@example.com", "student", "swqa"),
        new User("3", "Mariah", "Harris", "mariah.hairam@example.com", "teacher", "swqa")
  );

  private CampusApp app;
  private EmailSender emailSender;
  private UsersRepository usersRepository;

  @BeforeEach
  public void setUp() {
    usersRepository = mock(UsersRepository.class);
    emailSender = mock(EmailSender.class);
    app = new CampusApp(usersRepository, emailSender);
    when(usersRepository.getUsersByGroup("swqa")).thenReturn(initialUsers);
    when(usersRepository.getUsersByGroupAndRole("swqa", "teacher")).thenReturn(List.of(initialUsers.get(2)));
  }

  @Test
  public void testCreateGroup(){
    app.createGroup("bigdata");
    verify(usersRepository).createGroup("bigdata");
    verifyNoMoreInteractions(usersRepository);
    verifyNoMoreInteractions(emailSender);
  }


  @Test
  public void testSendEmailToGroup() {
    final var subject = "New campus!";
    final var body = "Hello everyone! We just created a new virtual campus!";
    app.sendMailToGroup("swqa", subject, body);
    verify(emailSender).sendEmail("john.doe@example.com", subject, body);
    verify(emailSender).sendEmail("jane.doe@example.com", subject, body);
    verify(emailSender).sendEmail("mariah.hairam@example.com", subject, body);

    verifyNoMoreInteractions(emailSender);
  }

  @Test
  public void testSendEmailToGroupRole() {
    final var subject = "Hey! Teacher!";
    final var body = "Let them students alone!!";
    app.sendMailToGroupRole("swqa", "teacher", subject, body);
    verify(emailSender).sendEmail("mariah.hairam@example.com", subject, body);
    verifyNoMoreInteractions(emailSender);
  }

  @Test
  public void testCreateUser() {
    // Arrange
    final String name = "John";
    final String surname = "Doe";
    final String email = "john.doe@example.com";
    final String role = "student";
    final String groupName = "swqa";

    // Act
    app.createUser(name, surname, email, role, groupName);

    // Assert
    verify(usersRepository).createUser(name, surname, email, role, groupName);
    verifyNoMoreInteractions(usersRepository);
    verifyNoMoreInteractions(emailSender);
  }

}
