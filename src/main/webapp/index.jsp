<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
    <head>
        <title>JSP - Hello World</title>
    </head>

    <body>
        <h1>
            <%= "Hello World!" %>
        </h1>

        <br/>

        <%
            String name = request.getParameter("name");
            if (name != null && !name.isEmpty()) {
        %>
                <h2>
                    Hello, <%= name %>!
                </h2>
        <%
            }else{
        %>

                <h2>
                    Hello, Guest!
                </h2>
        <%
        }
        %>


        <h2>
            Current Date and Time: <%= new java.util.Date() %>
        </h2>

        <%
            String nombre = "Cristiann";
        %>
            <h2>
                Hola, <%= nombre %>!
            </h2>


    </body>
</html>