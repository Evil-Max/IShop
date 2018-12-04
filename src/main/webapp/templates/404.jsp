<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page contentType="text/html;charset=utf-8" %>
<!doctype html>
<html lang="en">
<head>
    <%@ include file="parts/header.jsp" %>
</head>
<body>
    <%@ include file="parts/menu.jsp" %>
    <div class="container-fluid col-lg-4 px-4 py-4">

        <div class="row">
            <div class="justify-content-center mb-2">
                <h1>404</h1>
            </div>
        </div>
        <div class="row">
            <div class="justify-content-center mb-2">
                <h1>Страница не найдена!</h1>
            </div>
        </div>
        <div class="row">
            <div class="justify-content-center mb-2">
                <h1><a href="<c:url value='/' />">На главную</a> </h1>
            </div>
        </div>

    </div>
</body>
</html>