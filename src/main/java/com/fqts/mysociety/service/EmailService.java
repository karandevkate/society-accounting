package com.fqts.mysociety.service;

import com.fqts.mysociety.model.Journal;
import com.fqts.mysociety.model.SocietyMember;

public interface EmailService {

  void sendEmail(String to, String subject, String text);

  void sendSuperAdminNotification(SocietyMember societyMember);

  void sendJournalNotification(String to, Journal journal);

  void sendActivationEmail(String to, String fullName, String token);

  void sendRegistrationEmail(String email, String fullName);

  void sendApprovalRequestToAdmin(String fullName, String email, String mobile);

  void sendApprovalEmail(String email, String fullName);
}
