package com.servlets;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.Properties;

/**
 * Created by nikitos on 07.09.16.
 */
@WebServlet(name = "InfoSended", urlPatterns = "/excellent")
public class InfoSended extends HttpServlet {
    private final String INSERT_ORDER = "INSERT into orderNum.orderNumber (name, id) VALUE (?,?)";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String DownloadsDir = getServletContext().getInitParameter("DownloadsDir");
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");
        String name = request.getParameter("name");
        String id = request.getParameter("sess_id");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String post = request.getParameter("post");
        String sell = request.getParameter("sell");
        String No = null;
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/orderNum?useUnicode=true&characterEncoding=utf8", "root", "NUdx4RIA");
            PreparedStatement s = c.prepareStatement(INSERT_ORDER);
            s.setString(1, name);
            s.setString(2, id);
            s.execute();
            ResultSet rs = s.getGeneratedKeys();
            rs.next();
            No = rs.getString(1);
            rs.close();
            s.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        String letterText = "Здравствуйте, " + name + "!\nВы заказали 3d печать на сайте 3dprintthis.ru. Заказ поступил в обработку. Номер заказа: " + No + ".\n" +
                "В ближайшее время с вами свяжется оператор для согласования всех условий.\n\n" +
                "С уважением, 3dprintthis.";

        Float filament = Float.parseFloat(request.getParameter("filament"));
        int cost = (int) (filament * 6 * 6 / 1000 + 51);
        String count = request.getParameter("count");

        String content = "Деталь. В количестве " + count + " штук.\n" +
                "Общей стоимостью (без доставки)" + cost*Integer.parseInt(count) + " руб.\n" + cost +
                " руб/шт и временем выполнения заказа от " + (int) ((cost*Integer.parseInt(count)-50) / (60 * 12) + 1) + " до " + (int) ((cost*Integer.parseInt(count)-50) / (60 * 4) + 2) + " дней.\n" +
                "Информация о клиенте:\n" +
                "Имя: " + name +
                "\nemail: " + email +
                "\nтелефон: " + phone +
                "\nадрес: " + address +
                "\nспособ доставки: " + post +
                "\nспособ оплаты: " + sell +
                "\nплотность заполнения: " + request.getSession(false).getAttribute("fill_density") + "%";
        EmailSendMe(id, content, No, DownloadsDir);
        EmailSendUser(email, letterText);
        doGet(request, response);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        PrintWriter out = response.getWriter();

        out.println("<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\"/>\n" +
                "    <title>3dprintthis - 3d печать онлайн.</title>\n" +
                "    <link rel=\"shortcut icon\" type=\"image/png\" href=\"resourses/favicon.ico\">\n" +
                "\n" +
                "</head>\n" +
                "<body class=\"main\">\n" +
                "<div style=\"text-align: center;\">\n" +
                "    <a href=\"upload?start\"><img src=\"resourses/print_up1.png\" class=\"up_panel\" id=\"print_up\" style=\"margin-top: -20px;\"/></a>\n" +
                "    <a href=\"delivery\"><img src=\"resourses/send_up1.png\" class=\"up_panel\" id=\"send_up\" style=\"margin-top: -20px;\"/></a>\n" +
                "    <a href=\"contacts\"><img src=\"resourses/contacts_up1.png\" class=\"up_panel\" id=\"contacts_up\" style=\"margin-top: -20px;\"/></a>\n" +
                "    <a href=\"about_us\"><img src=\"resourses/about_us_up1.png\" class=\"up_panel\" id=\"about_us_up\" style=\"margin-top: -20px;\"/></a>\n" +
                "</div>\n" +
                "<div style=\"text-align: center; padding-top: 20%;\">\n" +
                "    <div style=\"font-size: 24\">" +
                "        Заказ успешно оформлен!" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "<script src='check.js'></script>\n" +
                "</html>");
        out.close();

    }

    public void EmailSendMe(String fileName, String content, String No, String DownloadsDir) {

        String subject = "Заказ №" + No;
        String smtpHost = "smtp.gmail.com";
        String address = "3dprintthis@mail.ru";
        String login = "3dprintthisbot@gmail.com";
        String password = "uVZQa7Nk";
        String smtpPort = "465";
        String attachment = DownloadsDir + fileName + ".stl";

        try {
            sendMultiMessage(login, password, address, address, content, subject, attachment, smtpPort, smtpHost);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void EmailSendUser(String address, String content) {

        String subject = "3d печать";
        String smtpHost = "smtp.gmail.com";
        String login = "3dprintthisbot@gmail.ru";
        String password = "uVZQa7Nk";
        String smtpPort = "465";

        try {
            sendSimpleMessage(login, password, address, address, content, subject, smtpPort, smtpHost);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void sendSimpleMessage(String login, String password, String from, String to, String content, String subject, String smtpPort, String smtpHost)
            throws MessagingException, UnsupportedEncodingException {
        Authenticator auth = new MyAuthenticator(login, password);

        Properties props = System.getProperties();
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.socketFactory.port", smtpPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.mime.charset", "UTF-8");
        Session session = Session.getDefaultInstance(props, auth);

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setText(content);
        Transport.send(msg);
    }

    public static void sendMultiMessage(String login, String password, String from, String to, String content, String subject, String attachment, String smtpPort, String smtpHost) throws MessagingException, UnsupportedEncodingException {

        Properties props = System.getProperties();
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.socketFactory.port", smtpPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.mime.charset", "UTF-8");

        Authenticator auth = new MyAuthenticator(login, password);
        Session session = Session.getDefaultInstance(props, auth);

        MimeMessage msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject, "UTF-8");

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(content, "text/plain; charset=" + "UTF-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(attachment);
        attachmentBodyPart.setDataHandler(new DataHandler(source));
        attachmentBodyPart.setFileName(MimeUtility.encodeText(source.getName()));
        multipart.addBodyPart(attachmentBodyPart);

        msg.setContent(multipart);

        Transport.send(msg);
    }

    static class MyAuthenticator extends Authenticator {
        private String user;
        private String password;

        MyAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            String user = this.user;
            String password = this.password;
            return new PasswordAuthentication(user, password);
        }
    }
}

