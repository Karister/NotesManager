<%@ tag body-content="empty" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="navigation" uri="http://arczynskiadam.pl/jsp/tlds/navigation" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags/_fragments" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="utils" uri="http://arczynskiadam.pl/jsp/tlds/utils" %>

<spring:theme code="theme.name" var="currentTheme"/>

<!-- workaround for Eclipse validation bug -->
<c:set var="currentLocale" value="${pageContext.response.locale}"/>
<!--end of workarround -->
		
<header class="top">
	<div class="topbar">
		<fragment:auth/>
		<span id="onlineCounter">
			<spring:message code="global.usersOnline"/>:&nbsp;${usersOnline}
		</span>
		<div class="dateHolder">
			<fmt:parseDate value="${utils:now()}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
			<fmt:formatDate value="${parsedDate}" type="date" pattern="dd/MM/yyyy" />
		</div>
	</div>
	<div class="menu">
		<nav class="buttonsBar">
			<c:forEach items="${themes}" var="theme">
				<c:url value="" var="themeUrl">
					<c:param name="theme" value="${theme}"/>
				</c:url>
				<a href="${themeUrl}">
					<span class="${theme} buttonBorder${theme eq currentTheme?" active":""}">
						<span class="${theme} themeholder${theme eq currentTheme?" active":""}"></span>
					</span>
				</a>
			</c:forEach>
		</nav>
		<nav class="buttonsBar">
			<c:forEach items="${locales}" var="locale">
				<c:url value="" var="langUrl">
					<c:param name="lang" value="${locale}"/>
				</c:url>
				<a href="${langUrl}">
					<span class="${locale} buttonBorder${locale eq currentLocale?" active":""}">
						<span class="${locale} flagholder dark"></span>
						<span class="${locale} flagholder${locale eq currentLocale?" active":""}"></span>
					</span>
				</a>
			</c:forEach>
		</nav>
		<nav class="mainmenu">
			<span class="notimplemented">
				<spring:message code="global.menu" />
			</span>	
		</nav>
	</div>
	<div class="clockHolder">
		<canvas class="clockBar" id="clock" width="75" height="75"></canvas>
	</div>
	<c:if test="${not empty breadcrumbs}">
		<div class="breadcrumbs">
			<navigation:breadcrumbs items="${breadcrumbs}" />
		</div>
	</c:if>	
</header>