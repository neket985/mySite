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
@WebServlet(name = "Send", urlPatterns = "/delivery")
public class Send extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        java.io.PrintWriter out = response.getWriter();

        out.println("<html> \n" +
                "<head> \n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /> \n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" /> \n" +
                "    <title>3D печать</title> \n" +
                "    <link rel=\"shortcut icon\" type=\"image/png\" href=\"resourses/favicon.ico\"> \n" +
                " \n" +
                "</head> \n" +
                "<body class=\"main\" > \n" +
                "<div style=\"text-align: center;\"> \n" +
                "   <a href=\"upload?start\"><img src=\"resourses/print_up1.png\" class=\"up_panel\" id=\"print_up\" style=\"margin-top: -20px;\"/></a>\n" +
                "   <a href=\"delivery\"><img src=\"resourses/send_up2.png\" class=\"up_panel\" id=\"send_up\" style=\"margin-top: -20px;\"/></a>\n" +
                "   <a href=\"contacts\"><img src=\"resourses/contacts_up1.png\" class=\"up_panel\" id=\"contacts_up\" style=\"margin-top: -20px;\"/></a>\n" +
                "   <a href=\"about_us\"><img src=\"resourses/about_us_up1.png\" class=\"up_panel\" id=\"about_us_up\" style=\"margin-top: -20px;\"/></a> \n" +
                "</div>\n" +
                "<table style=\"width: 100%; margin-top: 30px; padding-left: 10%; padding-right: 10%; font-size: 20px;\">\n" +
                "    <tr>\n" +
                "        <td colspan=\"3\" style=\"text-align: center; padding-bottom: 10px;\">КУРЬЕРСКАЯ ДОСТАВКА</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"width: 400px\">\n" +
                "            <img src=\"resourses/pochta.png\">\n" +
                "        </td>\n" +
                "        <td colspan=\"2\">\n" +
                "            Данный вид доставки доступен только по Москве.<br>\n" +
                "            Срок доставки от 1 до 2 дней.<br>\n" +
                "            Стоимость доставки 300 руб.\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td colspan=\"3\" style=\"text-align: center; padding-bottom: 10px; padding-top: 10px;\">ДОСТАВКА ПОЧТОЙ РОССИИ</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td colspan=\"2\">\n" +
                "            Доставка осуществляется по всей территории Российской Федерации.<br>\n" +
                "            1) По Москве срок доставки составляет 2 - 3 рабочих дня, а стоимость 200 руб.<br>\n" +
                "            2) По Московской области срок доставки может варьироваться в пределах от 3 до 10 дней. Стоимость такой доставки 300 руб.<br>\n" +
                "            3) В остальные регионы Российской Федерации стоимость и сроки доставки предоставляются по запросу.\n" +
                "        </td>\n" +
                "        <td style=\"width: 400px\">\n" +
                "            <img src=\"resourses/post.png\" >\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "   <tr>" +
                "       <td colspan=\"3\" style=\"text-align: center; padding-bottom: 10px; padding-top: 10px;\">" +
                "           ОПЛАТА" +
                "       </td>" +
                "   </tr>" +
                "   <tr>" +
                "       <td colspan=\"3\" style=\"text-align: center; \">" +
                "           На данный момент возможны два способа оплаты заказа.<br>" +
                "           1) Банковский перевод. Если у Вас есть банковская карта, вы можете оплатить заказ, " +
                "           переведя нужные средства на счёт, указанный в письме-подтверждении. " +
                "           Данное письмо приходит на адрес Вашей электронной почты, после оформления заказа.<br>" +
                "           2) Оплата через интернет кошелёк. Этот вариант подойдёт для тех, кто предпочитает оплату наличными. " +
                "           Перевод можно осуществить в любом терминале, либо через интернет." +
                "       </td>" +
                "   </tr>" +
                "</table>\n" +
                "</body> \n" +
                "<script src='check.js'></script> \n" +
                "</html>");
        out.close();
    }
}
