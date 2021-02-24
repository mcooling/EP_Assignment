<%--@elvariable id="films" type="model_beans.Film"--%>
<%@taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core"%>

<c:forEach items="${films}" var="f">
    Film ID: ${f.id}#
    Name: ${f.title}#
    Year: ${f.year}#
    Director: ${f.director}#
    Cast: ${f.stars}#
    Plot: ${f.review}
</c:forEach>


