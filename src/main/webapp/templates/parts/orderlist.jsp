<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page contentType="text/html;charset=utf-8" %>
<!-- Список заказов start -->
<div class="container-fluid col-lg-10 px-4" >
    <div class="row">
        <div class="container">
            <div class="row">
                <div class="col-md-8 d-flex mt-1 justify-content-start">
                    <h2>Список ваших заказов</h2>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="table-responsive">
            <table class="table table-striped table-sm my-4">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Адрес доставки</th>
                    <th>Сумма заказа</th>
                    <th>Способ доставки</th>
                    <th>Статус</th>
                </tr>
                </thead>
                <tbody>
                 <!--${orders}-->
                <c:forEach items="${orders}" var="order">
                <tr>
                    <td>${order.id}</td>
                    <td class="col-6">${order.address}</td>

                    <td>${order.summa}</td>
                    <td>${order.deliveryStatus.getName()}</td>
                    <td>${order.orderStatus.getName()}</td>
                </tr>
                </c:forEach>

                <!--<tr><td>Список заказов пуст</td></tr>-->

            </tbody>
            </table>
        </div>
    </div>
</div>
<!-- Список заказов -->
