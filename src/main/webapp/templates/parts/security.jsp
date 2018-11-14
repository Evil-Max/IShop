<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page trimDirectiveWhitespaces="true" %>
<c:choose>
<c:when test="${sessionScope['SPRING_SECURITY_CONTEXT'] != null}">
    <sec:authentication var="user" property="principal" />
    <c:set var="name" scope="session" value="${user.getFI()}"/>
    <c:set var="isAdmin" scope="session" value="${user.isAdmin()}"/>
</c:when>
<c:otherwise>
    <c:set var="user" scope="session" value="${null}"/>
    <c:set var="name" scope="session" value=""/>
    <c:set var="isAdmin" scope="session" value="${false}"/>
</c:otherwise>
</c:choose>
