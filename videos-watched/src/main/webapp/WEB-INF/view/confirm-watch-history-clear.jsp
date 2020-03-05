<%@ taglib prefix="app" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Clear watch history</title>
        <app:header></app:header>
    </head>
    <body>
        <app:navigation></app:navigation>
        <div class="container mt-3">
            <div class="row">
                <div class="col">
                    <div class="card">
                        <h5 class="card-header">Clear watch history</h5>
                        <div class="card-body">
                            <p class="card-text">Your are about to clear watch history of '${groupName}'.</p>
                            <form method="post">
                                <div class="mt-2">
                                    <a class="btn btn-secondary" href="/">Cancel</a>
                                    <button class="btn btn-warning" type="submit">Clear</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
