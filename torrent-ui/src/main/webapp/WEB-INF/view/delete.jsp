<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>Torrents</title>
    <app:header></app:header>
</head>
<body>
    <app:navigation></app:navigation>
    <div class="container mt-3">
        <div class="row mt-3">
            <div class="col">
                <div class="card">
                    <h5 class="card-header">Delete torrent</h5>
                    <div class="card-body">
                        <p class="card-text">Are you sure, you want to delete ${torrent.name}</p>
                        <form method="post" action="${deleteUrl}">
                            <input type="hidden" name="id" value="${torrent.id}">
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" id="deleteData" name="deleteData">
                                <label class="form-check-label" for="deleteData">Delete downloaded files</label>
                            </div>
                            <div class="mt-2">
                                <button type="submit" class="btn btn-danger">Delete</button>
                                <a class="btn btn-light" href="${cancelUrl}">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>