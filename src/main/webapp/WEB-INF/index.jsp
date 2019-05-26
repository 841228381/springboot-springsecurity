<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>--%>
<%@ taglib uri="http://www.springsecurity.org/jsp" prefix="security"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Insert title here</title>
</head>
<body>
Hello!!!
1、hasRole('ADMIN')是springsecurity自带的根据角色来动态判断显示
<%--引入标签是<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>--%>
2、buttonUrl="/delete1"是需要自己写标签来根据路径动态做处理
<%--引入标签<%@ taglib uri="http://www.springsecurity.org/jsp" prefix="security"%>--%>
<%--<sec:authorize access="hasRole('ADMIN')">
    <a href="">admin page</a>
</sec:authorize>--%>
<security:authorize buttonUrl="/delete1">
    <a href="">URL admin page</a>
</security:authorize>
</body>
</html>