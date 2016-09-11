package com.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by nikitos on 30.08.16.
 */
@WebServlet(name = "AboutUs", urlPatterns = "/about_us")
public class AboutUs extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        java.io.PrintWriter out = response.getWriter();
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
                "    <a href=\"contacts\"><img src=\"resourses/contacts_up1.png\" class=\"up_panel\" id=\"contacts_up\" style=\"margin-top: -20px;\"/></a>\n" +
                "    <a href=\"about_us\"><img src=\"resourses/about_us_up2.png\" class=\"up_panel\" id=\"about_us_up\" style=\"margin-top: -20px;\"/></a>\n" +
                "</div>\n" +
                "<div style=\"padding-right: 20%; padding-left: 20%; padding-top: 60px; font-size: 19px;\">\n" +
                "    &nbsp; &nbsp; &nbsp; &nbsp; 3dprintthis - молодая, развивающаяся компания. Мы печатаем объёмные детали на заказ.\n" +
                "    Для печати мы используем экологически чистый пластик PLA белого цвета.\n" +
                "    Его делают из кукурузы и сахарного тростника, поэтому он абсолютно безвреден.\n" +
                "    Печать производится на принтерах семейства RepRap, которые позволяют достигать точности 0,2 мм.<br>\n" +
                "    &nbsp; &nbsp; &nbsp; &nbsp; К сожалению, печать одноцветная и габариты напечатанной детали не должны выходить за пределы 170x200x80 мм.\n" +
                "    Если модель превышает данные габариты, мы можем разделить её на несколько частей, напечатать их по отдельности и склеить.\n" +
                "</div>\n" +
                "<div style=\"text-align: center; padding-top:30px;\">" +
                "   <img src=\"resourses/reprap.jpg\">" +
                "</div>" +
                "</body>\n" +
                "<script src='check.js'></script>\n" +
                "</html>");
        out.close();
    }
}
