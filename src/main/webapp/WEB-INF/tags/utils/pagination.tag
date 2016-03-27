<%@ tag body-content="empty" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" 		 uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="form"   	 uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring"	 uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>

<%@ attribute name="paginationData" required="true" type="pl.arczynskiadam.notesmanager.web.data.NotesPaginationData" %>

<c:set var="linkCore">
	/notesmanager/show/?${pageContext.request.queryString}
</c:set>

<div class="paginationRow">
	<div class="margin"></div>
	<div class="center">
		<div class="paginationHolder">
			<c:if test="${!paginationData.firstPage}">
				<c:url value="${linkCore}" var="pagedLink">
					<c:param name="p" value="${paginationData.currentPage - 1}"/>
				</c:url>
				<a href="${pagedLink}">
					<span class="pagingItem">&lt;</span>
				</a>
			</c:if>
			<c:if test="${paginationData.firstLinkedPage > 0}">
				<c:url value="${linkCore}" var="pagedLink">
					<c:param name="p" value="0"/>
				</c:url>
				<a href="${pagedLink}">
					<span class="pagingItem">1</span>
				</a>
			</c:if>
			<c:if test="${paginationData.firstLinkedPage > 1}">
				<span class="pagingDots">...</span>
			</c:if>
			<c:forEach begin="${paginationData.firstLinkedPage}" end="${paginationData.lastLinkedPage}" var="i">
				<c:choose>
					<c:when test="${paginationData.currentPage == i}">
						<span class="pagingItem active">${i + 1}</span>
					</c:when>
					<c:otherwise>
						<c:url value="${linkCore}" var="pagedLink">
							<c:param name="p" value="${i}"/>
						</c:url>
						<a href="${pagedLink}">
							<span class="pagingItem">${i + 1}</span>
						</a>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<c:if test="${paginationData.lastLinkedPage < paginationData.totalPages - 2}">
				<span class="pagingDots">...</span>
			</c:if>
			<c:if test="${paginationData.lastLinkedPage < paginationData.totalPages - 1}">
				<c:url value="${linkCore}" var="pagedLink">
					<c:param name="p" value="${paginationData.totalPages - 1}"/>
				</c:url>
				<a href="${pagedLink}">
					<span class="pagingItem">${paginationData.totalPages}</span>
				</a>
			</c:if>
			<c:if test="${!paginationData.lastPage}">
				<c:url value="${linkCore}" var="pagedLink">
					<c:param name="p" value="${paginationData.currentPage + 1}"/>
				</c:url>
				<a href="${pagedLink}">
					<span class="pagingItem">&gt;</span>
				</a>
			</c:if>
		</div>
	</div>
	<div class="margin">
		<span>
			<spring:message code="notes.listing.label.pageSize"/>:
		</span>
		<c:url value="/notesmanager/show" var="action" />
		<select class="notesPageSize" data-action="${action}">
			<c:forEach items="${notesPageSizes}" var="size">
				<option value="${size}"${size eq paginationData.pageSize?'selected="selected"':''}>${size}</option>
			</c:forEach>
		</select>
	</div>
</div>