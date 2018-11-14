<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page contentType="text/html;charset=utf-8" %>
<!-- Корзина start -->
<div class="container-fluid col-lg-10 px-4" >
    <div class="row">
        <div class="container">
            <div class="row">
                <div class="col-md-8 d-flex mt-1 justify-content-start">
                    <h2>Ваш заказ</h2>
                </div>
                <div class="col-md-3 mr-0">
                    <form method="post" action="<c:url value = '/order'/>">
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <button <c:if test="${cart.products == null}">disabled</c:if> class="btn btn-primary mt-3 float-right" type="submit" data-toggle="collapse" data-target="#collapseFilter" aria-expanded="false" aria-controls="collapseExample">
                            Оформить
                        </button>
                    </form>

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
                    <th>Наименование</th>
                    <th>Цена</th>
                    <th>Удалить </th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${cart.products}" var="product">
                <c:choose>
                <c:when test="${product.status ne 'NO_AVAILABLE'}">
                <tr>
                    <td>${product.id}</td>
                    <td id="product-name-${product.id}" class="col-6">${product.name}</td>

                    <td>${product.price}</td>
                    <td>
                        <form method="post" action="<c:url value = '/deleteProduct/${product.id}'/>">
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-secondary btn-sm ml-4">x</button>
                        </form>
                    </td>
                </tr>
                </c:when>
                <c:otherwise>
                    <tr><td>Корзина пуста</td></tr>
                </c:otherwise>
                </c:choose>
                </c:forEach>
                <!-- Итог -->
                <tr class="border-top-2 mt-2">
                    <td></td>
                    <td class="font-weight-bold">ИТОГО</td>
                    <td class="font-weight-bold">${cart.getCartSum()}</td>
                    <th>
                    <form method="post" action="<c:url value = '/clearCart'/>">
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <button type="submit" class="btn btn-secondary btn-sm">Очистить</button>
                    </form>
                    </th>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<!-- Корзина end -->
