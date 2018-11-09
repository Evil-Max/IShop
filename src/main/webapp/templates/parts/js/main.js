
$(function () {
  $('[data-toggle="popover"]').popover()
});
$('.popover-dismiss').popover({
  trigger: 'focus'
});



function getXmlHttp(){
  var xmlhttp;
  try {
    xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
  } catch (e) {
    try {
      xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    } catch (E) {
      xmlhttp = false;
    }
  }
  if (!xmlhttp && typeof XMLHttpRequest!='undefined') {
    xmlhttp = new XMLHttpRequest();
  }
  return xmlhttp;
}
/*
$(document).ajaxSend(function(e, xhr, options) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    xhr.setRequestHeader(header, token);
});
*/
function setCartSum() {
	var req = getXmlHttp();

	req.onreadystatechange = function() {
        if (req.readyState == 4) {
            if(req.status == 200) {
                //alert(req.responseText);
                var event = JSON.parse(req.responseText);
                $('#cart-sum').text('Всего '+event.sum+' руб.');
            };
        };
    };
	req.open('POST', '/getCartSum', true);
    var obj  = {};

    var csrfParameter = '${_csrf.parameterName}';
    var csrfToken = '${_csrf.token}';
    obj[csrfParameter]=csrfToken;

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    req.setRequestHeader(header, token);
	req.send(obj);  // отослать запрос
}


function addProduct(id) {

    var req = getXmlHttp();

    req.onreadystatechange = function() {
        // onreadystatechange активируется при получении ответа сервера
        if (req.readyState == 4) {
            // если запрос закончил выполняться

            if(req.status == 200) {
                // если статус 200 (ОК) - выдать ответ пользователю

                var event = JSON.parse(req.responseText);
                $('#alerts').html('');

                if (event.status != "") {
                    addAlert(event.status,'danger');
                } else {
                    $('#cart-sum').text('Всего '+event.sum+' руб.');
                    addAlert('Товар \''+$('#product-name-'+id).text()+'\' добавлен в корзину','success');
                };


            }
        }
    }
    req.open('POST', '/addProduct/'+id, true);
    var obj  = {};

    var csrfParameter = '${_csrf.parameterName}';
    var csrfToken = '${_csrf.token}';
    obj[csrfParameter]=csrfToken;

    obj['id'] = id;
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    req.setRequestHeader(header, token);
    req.send(obj);  // отослать запрос

}

function addAlert(message,atype) {
    $('#alerts').append(
        '<div class="alert alert-'+atype+' alert-dismissible fade show" id="my-alert" role="alert">' +
            '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
            '&times;</button>' + message + '</div>');
    $(function(){
        window.setTimeout(function(){
            $('#alerts').html('');
        },2000);
    });
}

