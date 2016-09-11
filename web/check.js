document.forms[0].onsubmit = function (e) {
    // console.log(arguments);
    var fillD = document.getElementsByName('fill_density')[0].value;
    if (fillD < 1 || fillD > 100 || isNaN(fillD)) {
        alert("В поле \"плотность заполнения\" должны быть введены числа от 1 до 100");
        return false;
    }
    if (document.getElementsByName('upfile')[0].files.length != 0) {
        var fileName = document.getElementsByName('upfile')[0].files[0].name;
        if (fileName.indexOf('.stl') >= 0 || fileName.indexOf('.STL') >= 0 || fileName.indexOf('.Stl') >= 0 || fileName.indexOf('.STl') >= 0 || fileName.indexOf('.sTL') >= 0 || fileName.indexOf('.stL') >= 0) {
            show();
        }
        else {
            alert("Расширение файла должно соответствовать формату STL");
            return false;
        }
    }
    else {
        alert("Выберите файл");
        return false;
    }
};

document.forms[1].onsubmit = function (e) {
    if (document.getElementById('name').value == '' || document.getElementById('email').value == '' ||
        document.getElementById('phone').value == '' || document.getElementById('address').value == '' ||
        (document.getElementsByName('post')[0].checked == false && document.getElementsByName('post')[1].checked == false) ||
        (document.getElementsByName('sell')[0].checked == false && document.getElementsByName('sell')[1].checked == false)) {
        alert('Пожалуйста, заполните все данные.');
        return false;
    }
    show();
};

function show() {
    if (document.getElementById('fon').style.display == '') {
        document.getElementById('fon').style.display = 'block';
        document.getElementById('dwnld').style.display = 'block';
    }
    else if (document.getElementById('fon').style.display == 'block') {
        document.getElementById('fon').style.display = 'none';
        document.getElementById('dwnld').style.display = 'none';
    }
}

function show2() {
    if (document.getElementById('fon2').style.display == '') {
        document.getElementById('fon2').style.display = 'block';
        document.getElementById('buy_form').style.display = 'block';
    }
    else if (document.getElementById('fon2').style.display == 'block') {
        document.getElementById('fon2').style.display = 'none';
        document.getElementById('buy_form').style.display = 'none';
    }
    else if (document.getElementById('fon2').style.display == 'none') {
        document.getElementById('fon2').style.display = 'block';
        document.getElementById('buy_form').style.display = 'block';
    }
}

function h(state) {
    document.getElementById(state).style.marginTop = '-20px';
}



		



