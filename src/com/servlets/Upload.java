package com.servlets;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * Created by nikitos on 15.08.16.
 */
@WebServlet(name = "Upload", urlPatterns = "/upload")
public class Upload extends HttpServlet {
    private boolean isMultipart;
    private String filePath;
    private int maxFileSize = 50 * 1024 * 1024;
    private int maxMemSize = 4 * 1024 * 1024;
    private File file;
    int fill_density = 40;
    int count = 1;
    int SessionTime = 5 * 60 * 60;

    public void init() {
        // Get the file location where it would be stored.
        filePath =
                getServletContext().getInitParameter("file-upload");
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, java.io.IOException {
        String DownloadsDir = getServletContext().getInitParameter("DownloadsDir");
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        java.io.PrintWriter out = response.getWriter();

        String id = request.getSession().getId();
        request.getSession(false).setMaxInactiveInterval(SessionTime);
        file = new File(DownloadsDir + id + ".stl");

        if (request.getQueryString() == null) {
            isMultipart = ServletFileUpload.isMultipartContent(request);
            if (!isMultipart) {
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet upload</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<p>No file uploaded</p>");
                out.println("</body>");
                out.println("</html>");
                return;
            }
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // maximum size that will be stored in memory
            factory.setSizeThreshold(maxMemSize);
            // Location to save data that is larger than maxMemSize.
            //factory.setRepository(new File("/Users/nikitos/Downloads"));

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);
            // maximum file size to be uploaded.
            upload.setSizeMax(maxFileSize);

            try {
                // Parse the request to get file items.
                List fileItems = upload.parseRequest(request);

                // Process the uploaded file items
                Iterator i = fileItems.iterator();

                while (i.hasNext()) {
                    FileItem fi = (FileItem) i.next();
                    if (!fi.isFormField()) {
                        fi.write(file);
                    } else if (fi.isFormField()) {
                        String fieldName = fi.getFieldName();
                        String val = fi.getString();
                        if (fieldName.equals("fill_density")) {
                            fill_density = Integer.parseInt(val);
                            request.getSession().setAttribute("fill_density", fill_density);
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        doGet(request, response);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");
        String id = request.getSession().getId();
        String Slic3rDir = getServletContext().getInitParameter("Slic3rDir");
        String DownloadsDir = getServletContext().getInitParameter("DownloadsDir");

        String str = request.getQueryString();
        java.io.PrintWriter out = response.getWriter();


        String[] cmd = new String[2];
        double filament_cost = 6.0;//руб/метр
        int is_new = 1;
        int max_k = 1;

        Float filament = null;
        String[] max_layers = new String[2];
        String all_layers = null;
        int layer = 0;

        if (str == null) {
            is_new = 1;
            cmd[0] = Slic3rDir + "slic3r "+ DownloadsDir + id + ".stl --load " + DownloadsDir + "config.ini";
            cmd[0] = cmd[0] + " --fill_density " + fill_density;
            cmd[0] = cmd[0] + " --output " + DownloadsDir + "output.gcode";
            cmd[1] = Slic3rDir + "slic3r " + DownloadsDir + id + ".stl --export-svg --output " + DownloadsDir + id + ".svg";
            Process proc = Runtime.getRuntime().exec(cmd[0]);
            try {
                proc.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            InputStream str1 = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(str1);
            BufferedReader br = new BufferedReader(isr);
            String line;


            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line.indexOf("Filament") >= 0) {
                    filament = Float.parseFloat(line.substring(line.indexOf(":") + 1, line.indexOf("mm")));
                    try {
                        request.getSession(false).setAttribute("filament", filament);
                    } catch (Exception e) {
                        Redir(response, id);
                        return;
                    }
                }
            }
            proc.destroy();
            if (filament == null) {
                System.out.println("Error: output error");
                response.sendRedirect("errors/read-file.html");
                return;
            } else {
                Process proc2 = Runtime.getRuntime().exec(cmd[1]);
                try {
                    proc2.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                InputStream str2 = proc2.getInputStream();
                InputStreamReader isr2 = new InputStreamReader(str2);
                BufferedReader br2 = new BufferedReader(isr2);
                String lineSVG;
                while ((lineSVG = br2.readLine()) != null) {
                    System.out.println(lineSVG);
                }
                proc2.destroy();
            }

            all_layers = SVG_READ(max_layers, id, DownloadsDir);
            try {
                request.getSession(false).setAttribute("all_layers", all_layers);
                request.getSession(false).setAttribute("max_layers", max_layers);
            } catch (Exception e) {
                Redir(response, id);
                return;
            }
            System.out.println(all_layers);
            System.out.println("max_num=" + max_layers[0]);
        } else if (str.indexOf("layer") >= 0) {
            is_new = 0;
            String s;
            if (str.indexOf("&") >= 0) {
                s = str.substring(str.indexOf("=") + 1, str.indexOf("&"));
            } else {
                s = str.substring(str.indexOf("=") + 1);
            }
            try {
                layer = Integer.parseInt(s);
            } catch (Exception e) {
                layer = 0;
            }
            try {
                all_layers = (String) request.getSession(false).getAttribute("all_layers");
                max_layers = (String[]) request.getSession(false).getAttribute("max_layers");
                filament = (Float) request.getSession(false).getAttribute("filament");
            } catch (Exception e) {
                Redir(response, id);
                return;
            }
            try {
                if (layer > Integer.parseInt(max_layers[0])) {
                    layer = Integer.parseInt(max_layers[0]);
                }
            } catch (Exception e) {
            }
            try {
                max_k = (int) request.getSession(false).getAttribute("max_k");
            } catch (Exception e) {
                Redir(response, id);
                return;
            }
        } else if (str.indexOf("start") >= 0) {
            Float s = (Float) request.getSession(false).getAttribute("filament");
            if (s != null) {
                response.sendRedirect("upload?layer=0");
                return;
            }
            is_new = 0;
        } else {
            System.out.println("Error: error in url");
            response.sendRedirect("upload?start");
            return;
        }
        out.println("<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" />\n" +
                "    <title>3D печать</title>\n" +
                "    <link rel=\"shortcut icon\" type=\"image/png\" href=\"resourses/favicon.ico\">\n" +
                "" +
                "</head>\n" +
                "<body class=\"main\" >" +
                "<div style=\"text-align: center;\">" +
                "   <a href=\"upload?start\"><img src=\"resourses/print_up2.png\" class=\"up_panel\" id=\"print_up\" style=\"margin-top: -20px;\"/><a>" +
                "   <a href=\"delivery\"><img src=\"resourses/send_up1.png\" class=\"up_panel\" id=\"send_up\" style=\"margin-top: -20px;\"/></a>" +
                "   <a href=\"contacts\"><img src=\"resourses/contacts_up1.png\" class=\"up_panel\" id=\"contacts_up\" style=\"margin-top: -20px;\"/></a>" +
                "   <a href=\"about_us\"><img src=\"resourses/about_us_up1.png\" class=\"up_panel\" id=\"about_us_up\" style=\"margin-top: -20px;\"/></a>" +
                "</div>" +
                "<table style=\"border: border-collapse; width: 100%; margin-top: 40px; \">\n" +
                "    <tr>\n" +
                "        <td class=\"center_m\" style=\"width: auto; padding-top:40px;padding-bottom:20px;\"></td>\n" +
                "        <td rowspan=\"5\" class=\"pctr\">\n <div style=\"text-align:center;\">");
        if (filament != null) {
            if (is_new == 1) {
                float x = Float.parseFloat(max_layers[1].substring(max_layers[1].indexOf("width=\"") + 7, max_layers[1].indexOf("\" h")));
                float y = Float.parseFloat(max_layers[1].substring(max_layers[1].indexOf("height=\"") + 8, max_layers[1].indexOf("\" x")));
                float z = (float) (Integer.parseInt(max_layers[0]) * 0.3);
                max_k = 1;
                if (x > 200) {
                    max_k = (int) ((x / 200) + 1);
                }
                if (y > 200 && max_k < (int) ((y / 200) + 1)) {
                    max_k = (int) ((y / 200) + 1);
                }
                try {
                    request.getSession(false).setAttribute("max_k", max_k);
                } catch (Exception e) {
                    Redir(response, id);
                    return;
                }
                if ((x > 170 || y > 200 || z > 80) && (x > 200 || y > 170 || z > 80) && (x > 80 || y > 170 || z > 200) && (x > 80 || y > 200 || z > 170) && (x > 170 || y > 80 || z > 200) && (x > 200 || y > 80 || z > 170)) {
                    out.println("<script>" +
                            "alert('Деталь слишком большая. Мы можем разделить её на несколько частей, напечатать по отдельности и склеить.');" +
                            "</script>");
                }
            }
            out.println("            <svg " + max_layers[1] + " style=\"transform: scale(" + (float) 2 / max_k + ", " + (float) 2 / max_k + ")\">\n" +
                    "          PM org.apache.catalina.core.ApplicationContext      " + getSVGlay(layer, all_layers) + "\n" +
                    "            </svg>\n ");

        }
        out.println("</div>" +
                "        </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td class=\"center_m\" style=\"width: auto; padding-top:40px;padding-bottom:20px;\">\n" +
                "            <form method=\"POST\" enctype=\"multipart/form-data\" action=\"upload\">\n" +
                "                <div style=\"padding-top:10px;padding-bottom:10px;\">ЗАГРУЗИТЬ STL ФАЙЛ</div>" +
                "                <div style=\"padding-top:10px;padding-bottom:10px;\"><input type=\"file\" name=\"upfile\" accept=\"STL\"></div>\n" +
                "                <div style=\"padding-top:10px;padding-bottom:10px;\">плотность заполнения: <input type=\"text\" name=\"fill_density\" value=\"40\" style=\"text-align:right; padding-right:10px; width:100px\"> %</div>" +
                "                <div style=\"padding-top:10px;padding-bottom:10px;\"><button type=\"submit\" style=\"background:transparent; outline: none; border:none\">" +
                "                                                                           <img src=\"resourses/send_button.png\">" +
                "                                                             </button></div></form>");
        if (filament != null) {
            int cost = (int) ((filament * filament_cost * 6) / 1000 + 51);
            out.println("<div class=\"center_m\">Стоимость данной детали (без учёта доставки): " +
                    cost + " руб.<br>" +
                    "Срок выполнения заказа от " + (int) ((cost-50)/ (60 * 12) + 1) + " до " + (int) ((cost-50) / (60 * 4) + 2) + " дней.</div>");
        }
        out.println("</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td rowspan=\"2\">");
        if (filament != null) {
            out.println("<form method=\"POST\" action=\"excellent\">" +
                    "   <input type=\"hidden\" name=\"filament\" value=\"" + filament + "\"/>" +
                    "   <input type=\"hidden\" name=\"sess_id\" value=\"" + id + "\"/>" +
                    "   <div style=\"text-align: center;\">" +
                    "       <my>количество: <input type=\"text\" name=\"count\" value=\"1\" style=\"text-align:right; padding-right:10px; width:100px\"> шт.</my>" +
                    "       <d style=\"background:transparent; border:none; outline: none;\" onclick=\"show2(); if(document.getElementsByName('count')[0].value==''){alert('Введите количество деталей.'); show2();}\">" +
                    "          <img src=\"resourses/buy_button.png\" align=\"middle\">" +
                    "       </d></div>" +
                    "       <div id=\"fon2\" class=\"fon2\" onclick=\"show2();\"></div>" +
                    "       <div id=\"buy_form\" class=\"buy_form\">" +
                    "           <div style=\"text-align:center; font-size: 24px; padding-bottom:20px; padding-top:20px;\">Оформление заказа</div>" +
                    "           <div class=\"buy_form_val\">Как к вам обращаться? <input type=\"text\" style=\"border-style: solid;border-width: 3px;\" width=\"40px\" name=\"name\" id=\"name\"/></div>" +
                    "           <div class=\"buy_form_val\">Введите свой email: <input type=\"text\" style=\"border-style: solid;border-width: 3px;\" width=\"40px\" name=\"email\" id=\"email\"/></div>" +
                    "           <div class=\"buy_form_val\">Введите контактный телефон: <input type=\"text\" style=\"border-style: solid;border-width: 3px;\" width=\"40px\" name=\"phone\" id=\"phone\"/></div>" +
                    "           <div class=\"buy_form_val\">Введите адрес доставки: <br><br><input type=\"text\" style=\"border-style: solid;border-width: 3px; width:500px\" name=\"address\" id=\"address\"/></div>" +
                    "           <div class=\"buy_form_val\">Выберите способ доставки: </div>" +
                    "               <div class=\"buy_form_val\"><input type=\"radio\" name=\"post\" value=\"почта\"/> Почтой России. От 200 руб.</div>" +
                    "               <div class=\"buy_form_val\"><input type=\"radio\" name=\"post\" value=\"курьер\"/> Курьером (только по Москве). 300 руб.</div>" +
                    "           <div class=\"buy_form_val\">Выберите способ оплаты: </div>" +
                    "               <div class=\"buy_form_val\"><input type=\"radio\" name=\"sell\" value=\"перевод\"/> Банковским переводом</div>" +
                    "               <div class=\"buy_form_val\"><input type=\"radio\" name=\"sell\" value=\"кошелёк\"/> Через интернет кошелёк</div>" +
                    "           <button type=\"submit\" style=\"background:transparent; outline: none; border:none\">" +
                    "                 <img src=\"resourses/send_email_button.png\">" +
                    "           </button>" +
                    "       </div>" +
                    "   </form>");
        }
        out.println("</td>\n" +
                "        <td></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>\n");
        if (filament != null) {
            out.println("       <form method=\"GET\" action=\"upload\">" +
                    "            0\n" +
                    "            <input type=\"range\" id=\"layer\" name=\"layer\" min=\"0\" max=\"" + max_layers[0] + "\" value=\"" + layer + "\">\n" +
                    max_layers[0] + "\n" + "<br>" +
                    "            <button type=\"submit\" style=\"background:transparent; outline: none; border:none\"> " +
                    "                  <img src=\"resourses/show_button.png\">" +
                    "            </button>" +
                    "       </form>");
        }
        out.println("        </td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "<div id=\"fon\" class=\"fon\"></div>" +
                "<div id=\"dwnld\" class=\"dwnld\"><img src=\"resourses/dwnld.png\"/>" +
                "</div>" +
                "</body>\n" +
                "<script src='check.js'></script>" +
                "</html>");
        out.close();
    }

    private static void Redir(HttpServletResponse response, String id) throws IOException {
        response.sendRedirect("errors/session_error.html");
        return;
    }

    private static String SVG_READ(String[] max_num, String id, String Downl) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        File f = new File(Downl + id + ".svg");
        BufferedReader fin = new BufferedReader(new FileReader(f));
        String line = new String();
        String max_layers;
        while ((max_layers = fin.readLine()) != null) {
            if (max_layers.indexOf("id=\"layer") >= 0) {
                max_num[0] = (max_layers.substring(max_layers.indexOf("id=\"layer") + 9, max_layers.indexOf("\" s")));
            } else if (max_layers.indexOf("<svg ") >= 0) {
                max_num[1] = (max_layers.substring(max_layers.indexOf("<svg") + 5, max_layers.indexOf(">")));
            } else if (max_layers.indexOf("style=\"fill: black\"") >= 0) {
                max_layers = max_layers.replace("fill: black", "fill: rgb(206, 46, 42)");
            }
            line += max_layers + "\n";
        }
        return line;
    }

    private static String getSVGlay(int num_lay, String all_lay) {
        int begin = all_lay.indexOf("<g id=\"layer" + num_lay);
        String svg_lay = all_lay.substring(begin);
        int end = svg_lay.indexOf("/g>") + 3;
        svg_lay = svg_lay.substring(0, end);
        return svg_lay;
    }
}

