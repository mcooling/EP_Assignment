<%--@elvariable id="films" type="model_beans.Film"--%>
<%@taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core"%>
{
    "headings": ["FILM ID", "TITLE", "YEAR", "DIRECTOR", "CAST", "PLOT"],
    "films": [
            <c:forEach items="${films}" var="f" varStatus="status">
            {
                    "id": "${f.id}",
                    "title": "${f.title}",
                    "year": "${f.year}",
                    "director": "${f.director}",
                    "stars": "${f.stars}",
                    "review": "${f.review}"
            } ${not status.last ? ',' :''}
            </c:forEach>
    ]
}
