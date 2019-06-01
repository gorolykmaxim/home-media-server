<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="downloadUrl" %>

<nav class="navbar sticky-top navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">Torrents</a>
    <c:if test="${not empty downloadUrl}">
        <a class="btn btn-outline-primary my-2 my-sm-0" href="${downloadUrl}">Download</a>
    </c:if>
</nav>