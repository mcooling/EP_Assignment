<?xml version="1.0" encoding="UTF-8"?>
<%--@elvariable id="films" type="model_beans.Film"--%>
<%@taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core"%>
<films>
  <c:forEach items="${films}" var="f">
    <film>
      <id>${f.id}</id>
      <title>${f.title}</title>
      <year>${f.year}</year>
      <director>${f.director}</director>
      <stars>${f.stars}</stars>
      <review>${f.review}</review>
    </film>
  </c:forEach>
</films>


