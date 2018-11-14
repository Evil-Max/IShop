<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page contentType="text/html;charset=utf-8" %>
<!-- Регистрация start -->
<div class="container-fluid col-lg-4 px-4 py-4">
    <div class="container">
        <div class="row">
            <div class="justify-content-start mb-2">
                <h3>Зарегистрируйтесь, пожалуйста</h3>
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
    <form action="<c:url value = '/register'/>" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <label for="login">Логин</label>
            <input type="text" class="form-control" id="login" name="login" placeholder="Login"/>


            <label for="password">Пароль</label>
            <input type="password" class="form-control" id="password" name="password" placeholder="Password"/>

            <label for="username">Имя</label>
            <input type="text" class="form-control" id="username" name="username" placeholder="Имя"/>


            <label for="usersurname">Фамилия</label>
            <input type="text" class="form-control" id="usersurname" name="usersurname" placeholder="Фамилия"/>

            <label for="userpatronymic">Отчество</label>
            <input type="text" class="form-control" id="userpatronymic" name="userpatronymic" placeholder="Отчество"/>

            <label for="address">Адрес</label>
            <input type="text" class="form-control" id="address" name="address"/>

            <label for="email">E-mail</label>
            <input type="email" class="form-control" id="email" placeholder="name@example.com" name="email"/>

            <button type="submit" class="btn btn-primary mt-2">Подтвердить</button>
    </form>
</div>
<!-- Регистрация end -->
