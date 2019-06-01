<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Whoops...</title>
        <app:header></app:header>
    </head>
    <body>
        <app:navigation></app:navigation>
        <div class="p-2">
            <div class="alert alert-danger" role="alert">
                <h4 class="alert-heading">Whoops...</h4>
                <p class="app-truncate">${error}</p>
                <hr>
                <p class="mb-0">See more details in page source.</p>
            </div>
        </div>
        <!--${stackTrace}-->
    </body>
</html>