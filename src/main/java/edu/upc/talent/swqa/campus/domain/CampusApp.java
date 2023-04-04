package edu.upc.talent.swqa.campus.domain;

public final class CampusApp {

  private final UsersRepository usersRepository;
  private final EmailSender emailSender;

  public CampusApp(final UsersRepository usersRepository, final EmailSender emailSender) {
    this.usersRepository = usersRepository;
    this.emailSender = emailSender;
  }

  public void createGroup(final String name) {
    usersRepository.createGroup(name);
  }

  public void createUser(
        final String name,
        final String surname,
        final String email,
        final String role,
        final String groupName
  ) {
    usersRepository.createUser(name, surname, email, role, groupName);
  }

  public void sendMailToGroupRole(
        final String groupName,
        final String onlyRole,
        final String subject,
        final String body
  ) {
    final var users = usersRepository.getUsersByGroupAndRole(groupName, onlyRole);
    users.forEach(u -> emailSender.sendEmail(u.email(), subject, body));
  }

  public void sendMailToGroup(final String groupName, final String subject, final String body) {
    final var users = usersRepository.getUsersByGroup(groupName);
    users.forEach(u -> emailSender.sendEmail(u.email(), subject, body));
  }
}
