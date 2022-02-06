package com.javaproject.time_scheduler;


import controller.MailSender;
import models.*;

public class StartMailSender {
    public static void main(String[] args) {

        /**
         * login information for the authentication on gmail
         */
        String username = "timescheduler2022@gmail.com";
        String password = "Java00PT!me$cheduler";

        MailSender sender = new MailSender();	// Instanzieren

        /**
         * Login into the gmail SMTP using the port number 587, which is for TLS
         */
        sender.login("smtp.gmail.com", "587", username, password);


        /**
         * Mail content formatted in HTML
         */
        //String message = message();

        /**
         * Using the send method to send an e-mail to all receivers
         */
        try {
            // sender.send("timescheduler2022@gmail.com", "Time Scheduler", event.getParticipants(event), "Reminder Mail: Time Scheduler", message);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to find get all participants from an event. Returns an unordered list formatted in HTML.
     * Return result will be added to the e-mail message.
     * @param event
     * @return
     */
    public static String allParticipants(Event event) {
        String participants = "<li style=\"list-style: none;\">None</li>";
        if(event.getParticipants().size() > 1) {
            for(int i=0; i < event.getParticipants().size(); i++) {
                participants = "<li style=\"list-style: none;\">" + event.getParticipants().get(i) + "</li>";
            }
        }
        return participants;
    }

    /**
     * Reminder e-mail message returning a string formatted in HTML.
     * @param event
     * @return
     */
    public static String message(Event event) {
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
