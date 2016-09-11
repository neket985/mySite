package com.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by nikitos on 31.08.16.
 */
@WebServlet(name = "Contacts", urlPatterns = "/contacts")
public class Contacts extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        PrintWriter out = response.getWriter();

        out.println("<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\"/>\n" +
                "    <title>3D печать</title>\n" +
                "    <link rel=\"shortcut icon\" type=\"image/png\" href=\"resourses/favicon.ico\">\n" +
                "\n" +
                "</head>\n" +
                "<body class=\"main\">\n" +
                "<div style=\"text-align: center;\">\n" +
                "    <a href=\"upload?start\"><img src=\"resourses/print_up1.png\" class=\"up_panel\" id=\"print_up\" style=\"margin-top: -20px;\"/></a>\n" +
                "    <a href=\"delivery\"><img src=\"resourses/send_up1.png\" class=\"up_panel\" id=\"send_up\" style=\"margin-top: -20px;\"/></a>\n" +
                "    <a href=\"contacts\"><img src=\"resourses/contacts_up2.png\" class=\"up_panel\" id=\"contacts_up\" style=\"margin-top: -20px;\"/></a>\n" +
                "    <a href=\"about_us\"><img src=\"resourses/about_us_up1.png\" class=\"up_panel\" id=\"about_us_up\" style=\"margin-top: -20px;\"/></a>\n" +
                "</div>\n" +
                "<div style=\"text-align: center; padding-top: 100px;\">\n" +
                "    <div style=\"padding-bottom: 20px;\">\n" +
                "        <a href=\"https://e.mail.ru\" target=\"new\"><img src=\"resourses/mail_icon.png\" align=\"center\"></a> почта: 3dprintthis@mail.ru\n" +
                "    </div>\n" +
                "    <div style=\"padding-bottom: 20px;\">\n" +
                "        <a href=\"https://vk.com/3dprintthis\" target=\"new\"><img src=\"resourses/vk_icon.png\" align=\"center\"></a> группа вконтакте: <a href=\"https://vk.com/3dprintthis\" target=\"new\">3dprintthis</a>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "<script src='check.js'></script>\n" +
                "</html>");
        out.close();
    }
}
