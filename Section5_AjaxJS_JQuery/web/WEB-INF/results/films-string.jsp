<%--@elvariable id="films" type="model_beans.Film"--%>
<%@taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core"%>
FILM ID#TITLE#YEAR#DIRECTORr#CAST#PLOT$
<c:forEach items="${films}" var="f">
    ${f.id}#
    ${f.title}#
    ${f.year}#
    ${f.director}#
    ${f.stars}#
    ${f.review}$
</c:forEach>


