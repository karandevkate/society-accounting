package com.fqts.mysociety.service.impl;

import com.fqts.mysociety.exception.EmailSendingException;
import com.fqts.mysociety.model.Journal;
import com.fqts.mysociety.model.SocietyMember;
import com.fqts.mysociety.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

  private static final String SUPER_ADMIN_EMAIL = "shivaniraskar0408@gmail.com";

  private final JavaMailSender mailSender;

  public EmailServiceImpl(JavaMailSender javaMailSender) {
    this.mailSender = javaMailSender;
  }
  
  @Override
  public void sendSuperAdminNotification(SocietyMember societyMember) {
    String subject = "New Society Registration: Approval Required";
    String text = "A new society '" + societyMember.getSociety().getSocietyName() + " to this " +
        societyMember.getSociety().getDetailedAddress() + " address."
        + "' has been registered and is awaiting for approval."
        + "\n Time: " + societyMember.getSociety().getRegisteredAt();

    sendEmail(SUPER_ADMIN_EMAIL, subject, text);
  }

  @Override
  public void sendJournalNotification(String to, Journal journal) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

    String subject = "Quarterly Maintenance Journal - " + sdf.format(journal.getTransactionDate());

    String text = "Dear Member,\n\n"
        + "Your quarterly maintenance journal has been generated.\n"
        + "Details:\n"
        + "Flat Number: " + journal.getFlat().getId().getFlatNumber() + "\n"
        + "Amount: ₹" + journal.getDebitAmount() + "\n"
        + "Date: " + sdf.format(journal.getTransactionDate()) + "\n"
        + "Description: " + journal.getNarration() + "\n\n"
        + "Regards,\n"
        + "Society Admin";

    sendEmail(to, subject, text);
  }

  @Override
  public void sendActivationEmail(String to, String fullName, String token) {
    String subject = "Activate Your Account - MySociety";
    String activationLink = "http://localhost:8081/members/activate?token=" + token;

    String message = "Hello " + fullName + ",\n\n"
        + "Thank you for registering with MySociety!\n\n"
        + "To complete your registration and activate your account, please click the link below:\n\n"
        + activationLink + "\n\n"
        + "If you did not sign up for MySociety, or you believe this email was sent to you in error, please disregard this message.\n\n"
        + "If you encounter any issues, feel free to reach out to our support team at support@mysociety.com.\n\n"
        + "Best regards,\n"
        + "The MySociety Team\n"
        + "www.mysociety.com";
    sendEmail(to, subject, message);
  }

  @Override
  public void sendEmail(String to, String subject, String body) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(body, false);

      mailSender.send(message);
    } catch (MessagingException e) {
      throw new EmailSendingException("Failed to send email to: " + to, e);
    }
  }

  @Override
  public void sendRegistrationEmail(String email, String fullName) {
    String subject = "Registration Successful - Pending Approval";
    String message = "Hello " + fullName + ",\n\n"
        + "Your registration was successful and is now pending admin approval. "
        + "You will receive a 6-digit PIN once approved.\n\n"
        + "Thank you!";
    sendEmail(email, subject, message);
  }

  @Override
  public void sendApprovalRequestToAdmin(String fullName, String email, String mobile) {
    String subject = "New Member Approval Request";
    String message = "Hello Admin,\n\n"
        + "A new member has registered and is awaiting approval.\n\n"
        + "Member Details:\n"
        + "Name: " + fullName + "\n"
        + "Email: " + email + "\n"
        + "Mobile: " + mobile + "\n\n"
        + "Please log in to the admin panel to approve or reject this request.\n\n"
        + "Thank you!";
    sendEmail("prathameshzende4@gmail.com", subject, message);  // Replace with actual admin email
  }

  @Override
  public void sendApprovalEmail(String email, String fullName) {
    String subject = "Your Registration is Approved!";
    String message = "Hello " + fullName + ",\n\n"
        + "Congratulations! Your registration has been approved.\n"
        + "You can now log in using your registered credentials.\n\n"
        + "Thank you!";
    sendEmail(email, subject, message);
  }
}


