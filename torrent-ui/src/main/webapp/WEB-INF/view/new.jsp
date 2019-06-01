<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Download torrents</title>
        <app:header></app:header>
    </head>
    <body>
        <app:navigation></app:navigation>
        <div class="container">
            <form action="${submitUrl}" method="post" class="p-3">
                <div class="form-group">
                    <label for="magnetLink">Magnet link</label>
                    <input type="text" id="magnetLink" name="magnetLink" class="form-control">
                </div>
                <div class="form-group">
                    <label for="downloadFolder">Download folder</label>
                    <input type="text" id="downloadFolder" name="downloadFolder" value="${defaultDownloadFolder}" class="form-control">
                </div>
                <button type="submit" class="btn btn-primary">Download</button>
            </form>
        </div>
    </body>
</html>