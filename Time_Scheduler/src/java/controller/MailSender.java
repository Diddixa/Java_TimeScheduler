package controller;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Mail sender class for reminders
 */
public class MailSender {

    /**
     * Connection of mail server
     */
    protected Session mailSession;

    /**
     * Method to connect to the mail server of the sender. In this case it is the gmail mail server.
     * @param smtpHost
     * @param smtpPort
     * @param username
     * @param password
     */
    public void login(String smtpHost, String smtpPort, String username, String password) {

        /**
         * Setup connection to the mail server
         */
        Properties props = new Properties();

        /**
         * Prints a debug protocol in the terminal console.
         */
        props.put("mail.debug", "true");

        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", smtpPort);

        /**
         * Sender login
         */
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        /**
         * Creates a session by props and login data of auth
         */
        this.mailSession = Session.getDefaultInstance(props, auth);
        System.out.println("Logged in!");
    }

    /**
     * Method to send an e-mail to specific users.
     * @param senderMail
     * @param senderName
     * @param receiverAddresses
     * @param subject
     * @param message
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void send(String senderMail, String senderName, String receiverAddresses, String subject, String message) throws MessagingException, UnsupportedEncodingException {
        if(mailSession == null) {
            throw new IllegalStateException("You have to log in first!");
        }

        /**
         * Sets the formats of the email.
         * MimeMessage makes it possible, to display the email properly
         */
        MimeMessage msg = new MimeMessage(mailSession);
        /**
         * Information are needed, so the email can be transfered by the
         * mail server properly, without any formatting problems
         */
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");

        /**
         * Tuple with the email and the Name of the Sender
         */
        msg.setFrom(new InternetAddress(senderMail, senderName));
        /**
         * E-mail addresses won't be altered
         */
        msg.setReplyTo(InternetAddress.parse(senderMail, false));
        /**
         * Set the subject text and use UTF-8
         */
        msg.setSubject(subject, "UTF-8");
        /**
         * Set the mail text message
         */
        msg.setText(message, "UTF-8", "html");
        /**
         * Set the sending date & time
         */
        msg.setSentDate(new Date());

        /**
         * Set the recipients and the type (cc, gcc etc.).
         * Set the address names won't be altered.
         */
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverAddresses, false));

        System.out.println("Send email...");
        Transport.send(msg); // send email
        System.out.println("Email sent.");
    }
}