package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.domain.UsersRepository;
import edu.upc.talent.swqa.campus.infrastructure.PostgreSqlUsersRepository;
import edu.upc.talent.swqa.campus.infrastructure.UsersDb;
import edu.upc.talent.swqa.campus.test.utils.Group;
import edu.upc.talent.swqa.campus.test.utils.UsersRepositoryState;
import edu.upc.talent.swqa.test.utils.DatabaseBackedTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;

public final class PostgreSqlUsersRepositoryTest extends DatabaseBackedTest implements UsersRepositoryTest {

  private PostgreSqlUsersRepository repository;

  @BeforeEach
  public void setUpDatabaseSchema() {
    db.update(UsersDb.groupsTableDml);
    db.update(UsersDb.usersTableDml);
    repository = new PostgreSqlUsersRepository(db);
  }

  @Override
  public UsersRepository getRepository() {
    return repository;
  }

  @Override
  @NotNull
  public UsersRepositoryState getUsersRepositoryState() {
    return new UsersRepositoryState(
          db.selectToSet(
                """
                select u.id, u.name, u.surname, u.email, u.role, g.name
                from users u join groups g on u.group_id = g.id""",
                (rs) -> new User(
                      rs.getString(1),
                      rs.getString(2),
                      rs.getString(3),
                      rs.getString(4),
                      rs.getString(5),
                      rs.getString(6)
                )
          ),
          db.selectToSet(
                "SELECT id, name FROM groups",
                (rs) -> new Group(rs.getInt(1), rs.getString(2))
          )
    );
  }

}