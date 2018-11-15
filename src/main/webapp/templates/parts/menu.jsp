<!--<#include "security.ftl">-->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page contentType="text/html;charset=utf-8" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <a class="navbar-brand col-md-2" href="<c:url value = '/'/>">Интернет магазин</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link mr-1" href="<c:url value = '/'/>">Главная<span class="sr-only">(current)</span></a>
            </li>
            <c:choose>
                <c:when test="${user != null}">
                    <li>
                        <form action="<c:url value = '/myorders'/>" method="post">
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <button class="btn btn-outline-light mr-1" type="submit">Мои заказы</button>
                        </form>
                    </li>
                    <li>
                        <form action="<c:url value = '/logout'/>" method="post">
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <button class="btn btn-outline-light" type="submit">Выход</button>
                        </form>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value = '/login'/>">Вход</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value = '/registration'/>">Регистрация</a>
                    </li>
                </c:otherwise>
            </c:choose>

        </ul>
    </div>
    <ul class="navbar-nav d-flex justify-content-end">
        <li>
            <span class="navbar-text mr-2">${name}</span>
        </li>
        <li class="border-left"></li>
        <li>
            <span class="navbar-text mx-2" id="cart-sum">Всего: 0 руб.</span>
        </li>
        <li>
            <form action="<c:url value = '/cart'/>" method="post">

                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <span action="<c:url value = '/cart'/>" <c:if test ="${user != null}">class="d-inline-block" tabindex="0" data-toggle="tooltip" title="Авторизуйтесь для оформления заказа"</c:if>>
                    <button class="btn btn-outline-light my-2 my-sm-0" type="submit"
                <c:if test ="${user == null}">disabled</c:if>>Корзина</button></span>

            </form>
        </li>
    </ul>
</nav>
<script>
setCartSum();
</script>