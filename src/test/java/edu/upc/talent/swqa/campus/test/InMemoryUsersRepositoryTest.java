package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.UsersRepository;
import edu.upc.talent.swqa.campus.test.utils.InMemoryUsersRepository;
import edu.upc.talent.swqa.campus.test.utils.UsersRepositoryState;

public final class InMemoryUsersRepositoryTest implements UsersRepositoryTest {

  private final InMemoryUsersRepository repository = new InMemoryUsersRepository(new UsersRepositoryState());

  @Override
  public UsersRepository getRepository() {
    return repository;
  }

  @Override
  public UsersRepositoryState getUsersRepositoryState() {
    return repository.state();
  }
}
