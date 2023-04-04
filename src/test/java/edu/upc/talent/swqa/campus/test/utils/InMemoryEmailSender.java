package edu.upc.talent.swqa.campus.test.utils;

import edu.upc.talent.swqa.campus.domain.EmailSender;

import java.util.Set;

public final class InMemoryEmailSender implements EmailSender {

  private final Set<SentEmail> sentEmails;

  public InMemoryEmailSender(final Set<SentEmail> sentEmails) {
    this.sentEmails = sentEmails;
  }

  @Override
  public void sendEmail(final String email, final String subject, final String body) {
    sentEmails.add(new SentEmail(email, subject, body));
  }

}
