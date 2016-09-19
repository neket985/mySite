<%--
  Created by IntelliJ IDEA.
  User: nikitos
  Date: 03.08.16
  Time: 10:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="styles.css"/>
    <title>3D печать. Печать на 3D принтере. Печать 3D модели. Распечатать 3D модель.</title>
    <link rel="shortcut icon" type="image/png" href="resourses/favicon.ico">
</head>
<body class="main">
<div style="text-align: center;">
    <a href="upload?start"><img src="resourses/print_up1.png" class="up_panel" id="print_up" style="margin-top: -20px;"/></a>
    <a href="delivery"><img src="resourses/send_up1.png" class="up_panel" id="send_up" style="margin-top: -20px;"/></a>
    <a href="contacts"><img src="resourses/contacts_up1.png" class="up_panel" id="contacts_up" style="margin-top: -20px;"/></a>
    <a href="about_us"><img src="resourses/about_us_up2.png" class="up_panel" id="about_us_up" style="margin-top: -20px;"/></a>
</div>
<div style="padding-right: 20%; padding-left: 20%; padding-top: 60px; font-size: 19px;">
    &nbsp; &nbsp; &nbsp; &nbsp; 3dprintthis - молодая, развивающаяся компания. Мы печатаем объёмные детали на заказ.
    Для печати мы используем экологически чистый пластик PLA белого цвета.
    Его делают из кукурузы и сахарного тростника, поэтому он абсолютно безвреден.
    Печать производится на принтерах семейства RepRap, которые позволяют достигать точности 0,2 мм.<br>
    &nbsp; &nbsp; &nbsp; &nbsp; К сожалению, печать одноцветная и габариты напечатанной детали не должны выходить за пределы 170x200x80 мм.
    Если модель превышает данные габариты, мы можем разделить её на несколько частей, напечатать их по отдельности и склеить.
</div>
<div style="text-align: center; padding-top:30px;">
   <img src="resourses/reprap.jpg">
</div>
</body>
<script src='check.js'></script>
</html>
