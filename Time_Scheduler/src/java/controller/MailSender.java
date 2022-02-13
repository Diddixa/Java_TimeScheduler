package controller;
import models.Event;
import models.MailStatus;
import models.Reminder;
import models.User;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Mail sender class for reminders
 */
public class MailSender {

    /**creating new session object to hold host data */
    Session newSession = null;

    /**creating new message object to hold email content */
    MimeMessage mimeMessage = null;
    /**
     * Logs into Gmail account and sends email to all participants
     */
    public  void sendMail() {
        String username = "javaooptimescheduler2022@gmail.com"; //"javaTimeScheduler2022@gmail.com";
        String password = "JavaT!me$cheduler"; //"JavaTimeScheduler2022";
        String mailHost = "smtp.gmail.com";
        Transport transport = null;
        try {
            transport = newSession.getTransport("smtp");
            transport.connect(mailHost, username, password);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();

            System.out.println("Mail successfully sent");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /*
     * Sets up email server, creates an email draft and sends the email to all participants
     *
     * @param event the event of subject
     * @param status of the event, which Mail layout will be drafted(creation, update, deletion email)
     */
    public static void sendEventMail(Event event, MailStatus status) throws MessagingException, IOException {
        MailSender mail = new MailSender();
        mail.setupServerProperties();
        mail.draftMail(event, status);
        mail.sendMail();
    }

    /**
     * Sets up email server, creates a reminder email draft and sends the mail to all participants
     * checks if current time is the reminder time
     *
     * @param user logged in user
     */
    public static void reminderMail(User user) throws MessagingException {
        for(Event event : user.getEvents()){
            if(user.getId() == event.getEventHostId()) {
                if (checkReminderTime(event)) {
                    MailSender mail = new MailSender();
                    mail.setupServerProperties();
                    mail.draftReminderMail(event);
                    mail.sendMail();
                }
            }
        }
    }

    /**
     * Drafts the email depending on event status which decides from different mail layouts
     *
     * @param event event of subject
     * @param status status of event
     */
    private void draftMail(Event event, MailStatus status) throws MessagingException, IOException {
        mimeMessage = new MimeMessage(newSession);

        MimeMultipart multipart = new MimeMultipart();
        MimeBodyPart attachment = new MimeBodyPart();
        if(!event.getAttachments().isEmpty())
        {
            attachment.attachFile((event.getAttachments().get(0)));
            multipart.addBodyPart(attachment);

        }

        MimeBodyPart text = new MimeBodyPart();

        /**
         * Information are needed, so the email can be transferred by the
         * mail server properly, without any formatting problems
         */
        //mimeMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
        mimeMessage.addHeader("format", "flowed");
        mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");

        for (User participant : event.getParticipants()) {
            try {
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(participant.getEmail()));
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        mimeMessage.setSubject(event.getName() + " " + event.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " + event.getStartTime() + "-" + event.getEndTime() + " " + event.getLocation());
        // mimeMessage.setText(MailSender.eventMessage(event, status),null, "html");
        text.setContent(MailSender.eventMessage(event, status), "text/HTML; charset=UTF-8");
        multipart.addBodyPart(text);

        mimeMessage.setContent(multipart);
    }

    /**
     * Drafts the reminder mail layout
     * @param  event event of subject
     */
    public void draftReminderMail(Event event) throws MessagingException {
        mimeMessage = new MimeMessage(newSession);
        /**
         * Information are needed, so the email can be transferred by the
         * mail server properly, without any formatting problems
         */
        mimeMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
        mimeMessage.addHeader("format", "flowed");
        mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");

        for (User participant : event.getParticipants()) {
            try {
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(participant.getEmail()));
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        try {
            mimeMessage.setSubject("Reminder: " + event.getName() + " " + event.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " + event.getStartTime() + "-" + event.getEndTime() + " " + event.getLocation());
            mimeMessage.setText(reminderMessage(event), null, "html");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        event.setReminder(Reminder.NONE);
        Database.editEvent(event);
    }

    /**
     * Sets up Google's SMTP server for email session
     */
    void setupServerProperties() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.port", "587"); //587
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        // properties.put("mail.smtp.starttls.required", "true");
        newSession = Session.getDefaultInstance(properties, null);
    }

    /**
     * Checks if the current time after reminder time
     *
     * @param event event of subject
     * @return Boolean if current time is after reminder time
     */
    public static boolean checkReminderTime(Event event){
        if(event.getReminder().equals(Reminder.NONE)){
            return false;
        }
        LocalDateTime eventTime = event.getDate().atTime(event.getStartTime());
        LocalDateTime reminderTime = eventTime.minusMinutes(event.getReminder().getMinutes());
        if(LocalDateTime.now().isAfter(eventTime)){
            return false;
        }
        return  LocalDateTime.now().isAfter(reminderTime);
    }


    /**
     * Method to find get all participants from an event. Returns an unordered list formatted in HTML.
     * Return result will be added to the e-mail message.
     * @param event
     * @return
     */
    public static String allParticipants(Event event) {
        String participants = "<li style=\"list-style: none;\">None</li>";
        if(!event.getParticipants().isEmpty()) {
            for(int i=0; i < event.getParticipants().size(); i++) {
                participants =  "<li style=\"list-style: none;\">" + event.participantList() + "</li>";
            }
        }
        return participants;
    }

    public static String eventMessage(Event event, MailStatus status){
        String mailHeadline = "";
        String mailText = "";
        switch (status) {
            case CREATED -> {
                mailHeadline = "New event!";
                mailText = "A new event has been created:";
            }
            case EDITED -> {
                mailHeadline = "Changed event information!";
                mailText = "The information for this event has been edited:";
            }
            case DELETED -> {
                mailHeadline = "No event anymore!";
                mailText = "The event has been deleted!";
            }
        }
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\" xmlns=\"https://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                " <head>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "  <meta name=\"x-apple-disable-message-reformatting\">\n" +
                "  <title></title>\n" +
                "  <!--[if mso]>\n" +
                "  <style>\n" +
                "   table {\n" +
                "    border-collapse: collapse;\n" +
                "    border-spacing: 0;\n" +
                "    border: none;\n" +
                "    margin: 0;\n" +
                "   }\n" +
                "   div, td {\n" +
                "    padding: 0;\n" +
                "   }\n" +
                "   div {\n" +
                "    margin: 0 !important;\n" +
                "   }\n" +
                "  </style>\n" +
                "  <noscript>\n" +
                "   <xml>\n" +
                "    <o:OfficeDocumentSettings>\n" +
                "     <o:PixelsPerInch>96</oPixelsPerInch>\n" +
                "     </o:OfficeDocumentSettings>\n" +
                "   </xml>\n" +
                "  </noscript>\n" +
                "  <![endif]-->\n" +
                "  <style>\n" +
                "   table, td, div, h1, p {\n" +
                "    font-family: Arial, sans-serif;\n" +
                "   }\n" +
                "  </style>\n" +
                "  </head>\n" +
                "  <body style=\"margin: 0; padding: 0; word-spacing: normal; background-color: #FFFFFF\">\n" +
                "   <div role=\"article\" aria-roledescription=\"email\" lang=\"en\" style=\"text-size-adjust:100%; -webkit-text-size-adjust:100%; -ms-text-size-adjust:100%; background-color:#FFFFFF;\">\n" +
                "    <table role=\"presentation\" style=\"width:100%; border:none; border-spacing:0;\">\n" +
                "     <tr>\n" +
                "      <td align=\"center\" style=\"padding: 0;\">\n" +
                "      <!--[if mso]>\n" +
                "       <table role=\"presentation\" align=\"center\" style=\"width:600px\">\n" +
                "        <tr>\n" +
                "         <td>\n" +
                "      <![endif]-->\n" +
                "       <table role=\"presentation\" style=\"width: 94%; max-width:600px; border:none; border-spacing:0; text-align:left; font-family:Arial,sans-serif; font-size:16px; line-height:22px; color:#363636;\">\n" +
                "       <!-- Header -->\n" +
                "        <tr>\n" +
                "         <td style=\"background:#7c1f4f; padding:30px; text-align:center;\">\n" +
                "          <h2 style=\"font-family:Lato,sans-serif; font-size:30px; line-height:32px; color:#FCECF4; text-align: left; text-transform:uppercase; margin:0;\">Time<br>Scheduler</h2>\n" +
                "         </td>\n" +
                "        </tr>\n" +
                "         <!-- Header end -->\n" +
                "        <tr>\n" +
                "        <td style=\"padding:30px; background-color:#ffffff;\">\n" +
                "         <h1 style=\"margin-top:0; margin-bottom:16px; font-size:26px; line-height:32px; font-weight:bold; color: #7c1f4f;\">" + mailHeadline + "</h1>\n" +
                "         <p>" + mailText + "</p>\n" +
                "         <h2 style=\"font-size:20px; font-weight:bold; color: #2c2c2c;\">" + event.getName() + "</h2>\n" +
                "         <table>\n" +
                "          <tr style=\"vertical-align: baseline;\">\n" +
                "           <td style=\"color: #acacac\">Date:</td>\n" +
                "           <td style=\"padding: 0 0 0 10px;\">" + event.getDate() + "</td>\n" +
                "          </tr>\n" +
                "          <tr style=\"vertical-align: baseline;\">\n" +
                "           <td style=\"color: #acacac\">Time:</td>\n" +
                "           <td style=\"padding: 0 0 0 10px;\">" + event.getStartTime() + " - " + event.getEndTime() + "</td>\n" +
                "          </tr>\n" +
                "          <tr style=\"vertical-align: baseline;\">\n" +
                "           <td style=\"color: #acacac\">Location:</td>\n" +
                "           <td style=\"padding: 0 0 0 10px;\">" + event.getLocation() + "</td>\n" +
                "          </tr>\n" +
                "          <tr style=\"vertical-align: baseline;\">\n" +
                "           <td style=\"color: #acacac\">Participants:</td>\n" +
                "           <td>\n" +
                "            <ul style=\"padding: 0 0 0 10px;\">\n" + allParticipants(event) +
                "            </ul>\n" +
                "           </td>\n" +
                "          </tr>\n" +
                "         </table>\n" +
                "        </td>\n" +
                "       </tr>\n" +
                "       <!-- Footer -->\n" +
                "       <tr>\n" +
                "        <td style=\"padding:20px; text-align:center; font-size:12px; background:#404040; color:#cccccc;\">\n" +
                "         <p style=\"margin:0;font-size:14px;line-height:20px;\">&reg; Time Scheduler <script>document.write(new Date().getFullYear())</script></p>\n" +
                "        </td>\n" +
                "       </tr>\n" +
                "       <!-- Footer end -->\n" +
                "      </table>\n" +
                "      <!--[if mso]>\n" +
                "         </td>\n" +
                "        </tr>\n" +
                "       </table>\n" +
                "      <![endif]-->\n" +
                "     </td>\n" +
                "    </tr>\n" +
                "   </table>\n" +
                "  </div>\n" +
                " </body>\n" +
                "</html>";
    }
    /**
     * Reminder e-mail message returning a string formatted in HTML.
     * @param event
     * @return
     */
    public static String reminderMessage(Event event) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\" xmlns=\"https://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                " <head>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "  <meta name=\"x-apple-disable-message-reformatting\">\n" +
                "  <title></title>\n" +
                "  <!--[if mso]>\n" +
                "  <style>\n" +
                "   table {\n" +
                "    border-collapse: collapse;\n" +
                "    border-spacing: 0;\n" +
                "    border: none;\n" +
                "    margin: 0;\n" +
                "   }\n" +
                "   div, td {\n" +
                "    padding: 0;\n" +
                "   }\n" +
                "   div {\n" +
                "    margin: 0 !important;\n" +
                "   }\n" +
                "  </style>\n" +
                "  <noscript>\n" +
                "   <xml>\n" +
                "    <o:OfficeDocumentSettings>\n" +
                "     <o:PixelsPerInch>96</oPixelsPerInch>\n" +
                "     </o:OfficeDocumentSettings>\n" +
                "   </xml>\n" +
                "  </noscript>\n" +
                "  <![endif]-->\n" +
                "  <style>\n" +
                "   table, td, div, h1, p {\n" +
                "    font-family: Arial, sans-serif;\n" +
                "   }\n" +
                "  </style>\n" +
                "  </head>\n" +
                "  <body style=\"margin: 0; padding: 0; word-spacing: normal; background-color: #FFFFFF\">\n" +
                "   <div role=\"article\" aria-roledescription=\"email\" lang=\"en\" style=\"text-size-adjust:100%; -webkit-text-size-adjust:100%; -ms-text-size-adjust:100%; background-color:#FFFFFF;\">\n" +
                "    <table role=\"presentation\" style=\"width:100%; border:none; border-spacing:0;\">\n" +
                "     <tr>\n" +
                "      <td align=\"center\" style=\"padding: 0;\">\n" +
                "      <!--[if mso]>\n" +
                "       <table role=\"presentation\" align=\"center\" style=\"width:600px\">\n" +
                "        <tr>\n" +
                "         <td>\n" +
                "      <![endif]-->\n" +
                "       <table role=\"presentation\" style=\"width: 94%; max-width:600px; border:none; border-spacing:0; text-align:left; font-family:Arial,sans-serif; font-size:16px; line-height:22px; color:#363636;\">\n" +
                "       <!-- Header -->\n" +
                "        <tr>\n" +
                "         <td style=\"background:#7c1f4f; padding:30px; text-align:center;\">\n" +
                "          <h2 style=\"font-family:Lato,sans-serif; font-size:30px; line-height:32px; color:#FCECF4; text-align: left; text-transform:uppercase; margin:0;\">Time<br>Scheduler</h2>\n" +
                "         </td>\n" +
                "        </tr>\n" +
                "         <!-- Header end -->\n" +
                "        <tr>\n" +
                "        <td style=\"padding:30px; background-color:#ffffff;\">\n" +
                "         <h1 style=\"margin-top:0; margin-bottom:16px; font-size:26px; line-height:32px; font-weight:bold; color: #7c1f4f;\">Don't forget your event!</h1>\n" +
                "         <p>This is a kindly reminder for your upcoming event:</p>\n" +
                "         <h2 style=\"font-size:20px; font-weight:bold; color: #2c2c2c;\">" + event.getName() + "</h2>\n" +
                "         <table>\n" +
                "          <tr style=\"vertical-align: baseline;\">\n" +
                "           <td style=\"color: #acacac\">Date:</td>\n" +
                "           <td style=\"padding: 0 0 0 10px;\">" + event.getDate() + "</td>\n" +
                "          </tr>\n" +
                "          <tr style=\"vertical-align: baseline;\">\n" +
                "           <td style=\"color: #acacac\">Time:</td>\n" +
                "           <td style=\"padding: 0 0 0 10px;\">" + event.getStartTime() + " - " + event.getEndTime() + "</td>\n" +
                "          </tr>\n" +
                "          <tr style=\"vertical-align: baseline;\">\n" +
                "           <td style=\"color: #acacac\">Location:</td>\n" +
                "           <td style=\"padding: 0 0 0 10px;\">" + event.getLocation() + "</td>\n" +
                "          </tr>\n" +
                "          <tr style=\"vertical-align: baseline;\">\n" +
                "           <td style=\"color: #acacac\">Participants:</td>\n" +
                "           <td>\n" +
                "            <ul style=\"padding: 0 0 0 10px;\">\n" + allParticipants(event) +
                "            </ul>\n" +
                "           </td>\n" +
                "          </tr>\n" +
                "         </table>\n" +
                "        </td>\n" +
                "       </tr>\n" +
                "       <!-- Footer -->\n" +
                "       <tr>\n" +
                "        <td style=\"padding:20px; text-align:center; font-size:12px; background:#404040; color:#cccccc;\">\n" +
                "         <p style=\"margin:0;font-size:14px;line-height:20px;\">&reg; Time Scheduler <script>document.write(new Date().getFullYear())</script></p>\n" +
                "        </td>\n" +
                "       </tr>\n" +
                "       <!-- Footer end -->\n" +
                "      </table>\n" +
                "      <!--[if mso]>\n" +
                "         </td>\n" +
                "        </tr>\n" +
                "       </table>\n" +
                "      <![endif]-->\n" +
                "     </td>\n" +
                "    </tr>\n" +
                "   </table>\n" +
                "  </div>\n" +
                " </body>\n" +
                "</html>";
    }
}