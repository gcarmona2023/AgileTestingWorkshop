package edu.upc.talent.swqa.campus.test.utils;

import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.domain.UsersRepository;

import java.util.List;

public record InMemoryUsersRepository(UsersRepositoryState state) implements UsersRepository {

  @Override
  public void createUser(
        final String name,
        final String surname,
        final String email,
        final String role,
        final String groupName
  ) {
    final var id = state.users().size() + 1;
    final var user = new User("" + id, name, surname, email, role, groupName);
    state.users().add(user);
  }

  @Override
  public void createGroup(final String name) {
    final var id = state.groups().size() + 1;
    state.groups().add(new Group(id, name));
  }

  @Override
  public List<User> getUsersByGroupAndRole(final String groupName, final String onlyRole) {
    return state.users().stream()
                .filter(user -> user.groupName().equals(groupName) && user.role().equals(onlyRole))
                .toList();
  }

  @Override
  public List<User> getUsersByGroup(final String groupName) {
    return state.users().stream()
                .filter(user -> user.groupName().equals(groupName))
                .toList();
  }
}
