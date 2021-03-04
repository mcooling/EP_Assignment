<%--@elvariable id="films" type="model_beans.Film"--%>
<%@taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core"%>
{
    "headings": ["Film ID", "Title", "Year", "Director", "Stars", "Review"],
    "films": [
            <c:forEach items="${films}" var="f">
            {
                    "id": "${f.id}",
                    "title": "${f.title}",
                    "year": "${f.year}",
                    "director": "${f.director}",
                    "stars": "${f.stars}",
                    "review": "${f.review}"
            },
            </c:forEach>
        {}
    ]
}
