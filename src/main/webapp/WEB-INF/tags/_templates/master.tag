<%@ tag description="Master Page template" body-content="scriptless" pageEncoding="UTF-8"%>

<%@ attribute name="additionalCSS" fragment="true" %>
<%@ attribute name="additionalJS" fragment="true" %>
<%@ attribute name="topBarContent" fragment="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags/_fragments" %>
<%@ taglib prefix="notes" tagdir="/WEB-INF/tags/notes" %>

<!DOCTYPE html>
<html>
<head>
	<title>Notes Manager</title>
	<meta charset="UTF-8"> 
	
	<security:csrfMetaTags />
	
	<link href="<c:url value="/themes/common/css/generic.css"/>" rel="stylesheet" type="text/css"/>
	<link href="<c:url value="/themes/common/css/main.css"/>" rel="stylesheet" type="text/css"/>
	<link href="<c:url value="/themes/common/css/header.css"/>" rel="stylesheet" type="text/css"/>
	<link href="<c:url value="/themes/common/css/footer.css"/>" rel="stylesheet" type="text/css"/>
	<link href="<c:url value="/themes/common/css/widgets.css"/>" rel="stylesheet" type="text/css"/>
	<link href="<c:url value="/themes/common/css/forms.css"/>" rel="stylesheet" type="text/css"/>
	<link href="<c:url value="/themes/common/css/globalMessages.css"/>" rel="stylesheet" type="text/css"/>

	<link href="${pageContext.request.contextPath}/themes/<spring:theme code="css.generic"/>" rel="stylesheet" type="text/css"/>
	<link href="${pageContext.request.contextPath}/themes/<spring:theme code="css.main"/>" rel="stylesheet" type="text/css"/>
	<link href="${pageContext.request.contextPath}/themes/<spring:theme code="css.header"/>" rel="stylesheet" type="text/css"/>
	<link href="${pageContext.request.contextPath}/themes/<spring:theme code="css.footer"/>" rel="stylesheet" type="text/css"/>
	<link href="${pageContext.request.contextPath}/themes/<spring:theme code="css.widgets"/>" rel="stylesheet" type="text/css"/>
	<link href="${pageContext.request.contextPath}/themes/<spring:theme code="css.forms"/>" rel="stylesheet" type="text/css"/>
	<link href="${pageContext.request.contextPath}/themes/<spring:theme code="css.globalMessages"/>" rel="stylesheet" type="text/css"/>

	<jsp:invoke fragment="additionalCSS" />
	
	<script src="${pageContext.request.contextPath}/js/jquery/jquery-2.1.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/csrf.js"></script>
	<script src="${pageContext.request.contextPath}/js/clock.js"></script>
	<script src="${pageContext.request.contextPath}/js/dev.js"></script>
	
	<jsp:invoke fragment="additionalJS" />
</head>
<body>
	<div id="placeholder"></div>
	<fragment:header />
	<div class="master">
		<div class="page">
			<jsp:doBody />
		</div>
	</div>
	<fragment:footer />
</body>
</html>

