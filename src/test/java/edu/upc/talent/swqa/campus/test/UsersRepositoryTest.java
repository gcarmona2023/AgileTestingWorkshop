package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.domain.UsersRepository;
import edu.upc.talent.swqa.campus.test.utils.Group;
import edu.upc.talent.swqa.campus.test.utils.UsersRepositoryState;
import static edu.upc.talent.swqa.util.Utils.plus;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface UsersRepositoryTest {

  public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException(String message) {
      super(message);
    }
  }

  UsersRepository getRepository();

  UsersRepositoryState getUsersRepositoryState();

  default void setInitialState(final UsersRepositoryState initialState) {
    initialState.groups().forEach((group) -> getRepository().createGroup(group.name()));
    initialState.users()
                .stream()
                .sorted(Comparator.comparing(User::id))
                .forEach((user) ->
                               getRepository().createUser(
                                     user.name(),
                                     user.surname(),
                                     user.email(),
                                     user.role(),
                                     user.groupName()
                               ));
  }

  default void assertExpectedFinalState(final UsersRepositoryState expectedFinalState) {
    assertEquals(expectedFinalState, getUsersRepositoryState());
  }

  UsersRepositoryState defaultInitialState = new UsersRepositoryState(
        Set.of(
              new User("1", "John", "Doe", "john.doe@example.com", "student", "swqa"),
              new User("2", "Jane", "Doe", "jane.doe@example.com", "student", "swqa"),
              new User("3", "Mariah", "Harris", "mariah.hairam@example.com", "teacher", "swqa")
        ),
        Set.of(new Group(1, "swqa"))
  );

  @Test
  default void testGetUsersByGroup() {
    setInitialState(defaultInitialState);
    final var actual = getRepository().getUsersByGroup("swqa");
    assertEquals(defaultInitialState.users(), new HashSet<>(actual));
    assertExpectedFinalState(defaultInitialState);
  }

  @Test
  default void testCreateUser() {
    setInitialState(defaultInitialState);
    final var name = "Jack";
    final var surname = "Doe";
    final var email = "jack.doe@example.com";
    final var role = "student";
    final var groupName = "swqa";
    final var expectedNewUser = new User("4", name, surname, email, role, groupName);
    final var expected =
          new UsersRepositoryState(plus(defaultInitialState.users(), expectedNewUser), defaultInitialState.groups());
    getRepository().createUser(name, surname, email, role, groupName);
    assertExpectedFinalState(expected);
  }

  @Test
  //@Disable
  default void testCreateUserFailsIfGroupDoesNotExist() {
    setInitialState(defaultInitialState);
    final var groupName = "non-existent";

    final var exception = assertThrows(RuntimeException.class, () ->
          getRepository().createUser("a", "b", "a.b@example.com", "student", groupName)
    );

    assertEquals("Group " + groupName + " does not exist", exception.getMessage());
    assertExpectedFinalState(defaultInitialState);
  }

  @Test
  default void testGetUsersByGroupAndRole() {
    // Arrange
    UsersRepository repository = getRepository();
    String groupName = "swqa";
    String role = "student";
    setInitialState(defaultInitialState);

    // Act
    List<User> actualUsers = repository.getUsersByGroupAndRole(groupName, role);

    // Assert
    User expectedUser = new User("2", "Jane", "Doe", "jane.doe@example.com", "student", "swqa");
    assertEquals(expectedUser, actualUsers.get(0));
  }
}



