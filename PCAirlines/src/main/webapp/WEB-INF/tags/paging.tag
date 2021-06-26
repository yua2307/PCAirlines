<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ tag import="org.springframework.util.StringUtils"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="pagedListHolder" required="true"
              type="org.springframework.beans.support.PagedListHolder"%>
<%@ attribute name="pagedLink" required="true" type="java.lang.String"%>
      <link rel="stylesheet"
      href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<div class="justify-content-center">
<c:if test="${pagedListHolder.pageCount >= 1}">
    <ul class="pagination justify-content-center">
        <c:if test="${!pagedListHolder.firstPage}">
            <li class="page-item" ><a class="page-link"
                    href="<%=StringUtils.replace(pagedLink, "~", String.valueOf(pagedListHolder.getPage() - 1))%>"> <- Previous </a></li>
        </c:if>
        <c:if test="${pagedListHolder.firstLinkedPage > 0}">
            <li><a class="page-link" href="<%=StringUtils.replace(pagedLink, "~", "0")%>">1</a></li>
        </c:if>
        <c:if test="${pagedListHolder.firstLinkedPage > 1}">
            <li class="page-item"><span class="pagingDots">...</span>
            <li>
        </c:if>
        <c:forEach begin="${pagedListHolder.firstLinkedPage}"
                   end="${pagedListHolder.lastLinkedPage}" var="i">
            <c:choose>
                <c:when test="${pagedListHolder.page == i}">
                    <li class=" page-item active"><a class="page-link" href="#">${i+1}</a></li>
                </c:when>
                <c:otherwise>
                    <li class="page-item"><a class="page-link"
                       href="<%=StringUtils.replace(pagedLink, "~", String.valueOf(jspContext.getAttribute("i")))%>">${i+1}</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:if
            test="${pagedListHolder.lastLinkedPage < pagedListHolder.pageCount - 2}">
            <li class="page-item"><span class="pagingDots">...</span></li>
        </c:if>
        <c:if
            test="${pagedListHolder.lastLinkedPage < pagedListHolder.pageCount - 1}">
            <li class="page-item"><a class="page-link"
                    href="<%=StringUtils.replace(pagedLink, "~", String.valueOf(pagedListHolder.getPageCount() - 1))%>">${pagedListHolder.pageCount}</a></li>
        </c:if>
        <c:if test="${!pagedListHolder.lastPage}">
            <li class="page-item"><a class="page-link"
                    href="<%=StringUtils.replace(pagedLink, "~", String.valueOf(pagedListHolder.getPage() + 1))%>">Next -></a></li>
        </c:if>
    </ul>
</c:if>
</div>