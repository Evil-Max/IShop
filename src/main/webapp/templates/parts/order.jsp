<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page contentType="text/html;charset=utf-8" %>
<!-- Заказ start -->
<div class="container-fluid col-lg-4 px-4 py-4" >
    <div class="container">
        <div class="row">
            <div class="justify-content-start mb-2">
                <h3>Заполните параметры доставки</h3>
                <h6><i>(Сумма заказа = ${ordersum})</i></h6>
            </div>

        </div>
    </div>
    <c:if test="${errormessage != null}">
        <div class="row">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${errormessage}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </div>
    </c:if>
    <form method="post" action="<c:url value = '/confirm'/>">

        <label for="formFIO" class="font-weight-bold">ФИО получателя</label>
        <input type="text" class="form-control" id="formFIO" name="fio" placeholder="Фамилия Имя Отчество" value="${fio}"/>
        <label for="formAddress" class="font-weight-bold">Адрес доставки</label>
        <input type="text" class="form-control" id="formAddress" name="address" value="${address}"/>

        <label for="formDelivery" class="font-weight-bold">Способ доставки</label>
        <select id="formDelivery" class="form-control" name="delivery">
            <option>Почтой</option>
            <option>Курьером</option>
            <option>Самовывоз</option>
        </select>

        <label for="formEmail" class="font-weight-bold">E-mail для уведомлений</label>
        <input type="email" class="form-control" id="formEmail" name="email" placeholder="name@example.com" value="${email}"/>

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary mt-2">Подтвердить</button>
    </form>
</div>
<!-- Заказ end -->
