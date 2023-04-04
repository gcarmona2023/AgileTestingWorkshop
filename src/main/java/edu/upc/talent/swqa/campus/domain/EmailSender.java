package edu.upc.talent.swqa.campus.domain;

public interface EmailSender {
  void sendEmail(final String email, final String subject, final String body);
}
