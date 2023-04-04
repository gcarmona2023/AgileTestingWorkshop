package edu.upc.talent.swqa.campus.infrastructure;

import edu.upc.talent.swqa.campus.domain.EmailSender;

public final class ConsoleEmailSender implements EmailSender {
  @Override
  public void sendEmail(final String email, final String subject, final String body) {
    System.out.println("To: " + email + "\nSubject: " + subject + "\nBody:\n" + body + "\n");
  }
}
