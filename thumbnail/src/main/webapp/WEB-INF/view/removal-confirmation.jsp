<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>Thumbnail</title>
    <app:header></app:header>
</head>
    <body>
        <app:navigation></app:navigation>
        <div class="mdc-dialog mdc-dialog--open">
            <div class="mdc-dialog__container">
                <div class="mdc-dialog__surface">
                    <form method="post" action="${removalUrl}">
                        <h2 class="mdc-dialog__title">Delete confirmation</h2>
                        <div class="mdc-dialog__content">
                            <p>${message}</p>
                        </div>
                        <footer class="mdc-dialog__actions">
                            <button type="submit" class="mdc-button mdc-button--raised mdc-dialog__button">
                                <div class="mdc-button__ripple"></div>
                                Delete
                            </button>
                            <a class="mdc-button mdc-dialog__button" href="/">
                                <div class="mdc-button__ripple"></div>
                                <span class="mdc-button__label">Cancel</span>
                            </a>
                        </footer>
                    </form>
                </div>
            </div>
            <div class="mdc-dialog__scrim"></div>
        </div>
        <script>
            mdc.autoInit();
        </script>
    </body>
</html>