<%--@elvariable id="films" type="model_beans.Film"--%>
<%@taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core"%>
{
    "films": [
            <c:forEach items="${films}" var="f">
            {
                    "id": "${f.id}",
                    "title": "${f.title}",
                    "year": "${f.year}",
                    "director": "${f.director}",
                    "stars": "${f.stars}",
                    "review": "${f.getReview()}"
            },
            </c:forEach>
        {}
    ]
}
