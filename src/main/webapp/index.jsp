<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="refresh" content="0;url=../static/login.html">
    <title>职工考勤管理系统</title>
</head>
<body>
    <c:if test="${empty sessionScope.currentUser}">
        <c:redirect url="/static/login.html"/>
    </c:if>
    <c:if test="${not empty sessionScope.currentUser}">
        <c:redirect url="/static/pages/index.html"/>
    </c:if>
</body>
</html>
