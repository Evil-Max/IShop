<!-- Список товаров start -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page contentType="text/html;charset=utf-8" %>
<div class="container-fluid">
    <div class="row">
        <nav class="col-md-2 d-none d-md-block bg-light sidebar">
            <div class="sidebar-sticky">
                <ul class="nav flex-column">
                    <li class="nav-item border-bottom mt-2">
                        <p class="font-weight-bold">Категории </p>
                    </li>
                    <c:forEach items="${categories}" var="category">
                        <li class="nav-item">
                            <a class="nav-link col-md-10 d-inline-block text-truncate <c:if test="${activeCategory==category.id}">active font-weight-bold</c:if>"href="<c:url value = "/category/${category.id}"/>">
                                ${category.name}
                            </a>
                       </li>
                    </c:forEach>
                </ul>
            </div>
        </nav>

        <div class="col-lg-10 px-4">
            <div class="container">
                <div class="row">
                    <div class="col-md-8 d-flex justify-content-start">
                        <h2>Список товаров</h2>
                    </div>
                    <div class="col-md-3 mr-0">
                        <button class="btn btn-primary mt-1 float-right"
                                type="button" data-toggle="collapse" data-target="#collapseFilter" aria-expanded="false" aria-controls="collapseExample">
                            Фильтр
                        </button>

                    </div>
                </div>
            </div>
            <div class="row">
                <div id="alerts" class="col-md-8 mr-0"></div>
            </div>

            <!--Фильтр-->
            <div class="container">
                <div class="row">
                    <div class="collapse form-group" id="collapseFilter">
                        <form action="<c:url value = "/filter"/>" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <!--Фильтр цена-->
                            <div class="my-2 form-group">
                                <div>
                                    <label class="col-form-label mx-sm-3 mb-2 font-weight-bold">Цена</label>
                                </div>
                                <div class="row ml-2">
                                    <div class="mx-sm-3 col-form-label">
                                        От
                                    </div>
                                    <div class="mx-sm-3">
                                        <input type="text" class="form-control mx-sm-3" name="price1" placeholder="Цена от">
                                    </div>
                                    <div class="mx-sm-3 col-form-label">
                                        До
                                    </div>
                                    <div>
                                        <input type="text" class="form-control mx-sm-3" name="price2" placeholder="Цена до">
                                    </div>
                                </div>
                            </div>

                            <!--Фильтр количество-->
                            <div class="my-2">
                                <div>
                                    <label class="col-form-label mx-sm-3 mb-2 font-weight-bold">Количество</label>
                                </div>
                                <div class="row ml-2">
                                    <div class="mx-sm-3 col-form-label">
                                        От
                                    </div>
                                    <div class="mx-sm-3">
                                        <input type="text" class="form-control mx-sm-3" name="qnty1" placeholder="Кол-во от">
                                    </div>
                                    <div class="mx-sm-3 col-form-label">
                                        До
                                    </div>
                                    <div>
                                        <input type="text" class="form-control mx-sm-3" name="qnty2" placeholder="Кол-во до">
                                    </div>
                                </div>
                            </div>
                            <!--Фильтр статус-->
                            <div class="my-2">
                                <div>
                                    <label class="col-form-label mx-sm-3 mb-2 font-weight-bold">Статус</label>
                                </div>
                                <div>
                                    <select class="form-control ml-3" name="status">
                                        <option></option>
                                        <option>В наличии</option>
                                        <option>Под заказ</option>
                                    </select>
                                </div>
                            </div>
                            <!--Фильтр атрибуты-->
                            <!--TODO-->
                            <c:if test="${activeCategory!=0}">
                                ${activeCategory}
                            </c:if>
                            <div class="form-group row ml-2">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <button type="submit" class="btn btn-primary collapsed my-2" data-toggle="collapse" data-target="#collapseFilter">Применить</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <!--Список товаров-->
            <div class="table-responsive">
                <table class="table table-striped table-sm">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Наименование</th>
                        <th>Цена</th>
                        <th>В наличии</th>
                        <th>Подробно</th>
                        <th>В корзину</th>
                    </tr>
                </thead>
                <tbody>
                    
                    <c:forEach items="${products}" var="product">
                    <c:choose>
                    <c:when test="${product.getStatus() ne 'NO_AVAILABLE'}">
                        <tr>
                            <td>${product.id}</td>
                            <td id="product-name-${product.id}">${product.name}</td>
                            <td>${product.price}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${product.getStatus() eq 'AVAILABLE'}">В наличии</c:when>
                                    <c:when test="${product.getStatus() eq 'ON_ORDER'}">Под заказ</c:when>
                                    <c:otherwise>Нет данных</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                            <a tabindex="0" class="btn btn-secondary btn-sm" role="button" data-toggle="popover" data-trigger="focus"
                               data-content="${product.attributesList()}">?</a>
                            </td>
                            <th>
                                <button class="btn btn-primary btn-sm" onclick="addProduct(${product.id});">+</button>
                            </th>

                        </tr>
                    </c:when>
                    </c:choose>
                    </c:forEach>
                    
                </tbody>
                </table>
            </div>

        </div>
    </div>
</div>
<c:if test="${message != null}">
    <script>
    addAlert('${message}','success');
    </script>
</c:if>