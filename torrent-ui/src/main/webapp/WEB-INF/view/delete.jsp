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
    <div class="mdc-dialog mdc-dialog--open">
        <div class="mdc-dialog__container">
            <div class="mdc-dialog__surface">
                <form method="post" action="${deleteUrl}">
                    <input type="hidden" name="id" value="${torrent.id}">
                    <h2 class="mdc-dialog__title">Delete torrent</h2>
                    <div class="mdc-dialog__content">
                        <p>Are you sure, you want to delete ${torrent.name}</p>
                        <div class="mdc-form-field">
                            <div class="mdc-checkbox" data-mdc-auto-init="MDCCheckbox">
                                <input type="checkbox" class="mdc-checkbox__native-control" id="deleteData" name="deleteData"/>
                                <div class="mdc-checkbox__background">
                                    <svg class="mdc-checkbox__checkmark" viewBox="0 0 24 24">
                                        <path class="mdc-checkbox__checkmark-path" fill="none" d="M1.73,12.91 8.1,19.28 22.79,4.59"/>
                                    </svg>
                                    <div class="mdc-checkbox__mixedmark"></div>
                                </div>
                                <div class="mdc-checkbox__ripple"></div>
                            </div>
                            <label for="deleteData">Delete downloaded files</label>
                        </div>
                    </div>
                    <footer class="mdc-dialog__actions">
                        <button type="submit" class="mdc-button mdc-button--raised mdc-dialog__button">
                            <div class="mdc-button__ripple"></div>
                            Delete
                        </button>
                        <a class="mdc-button mdc-dialog__button" href="${cancelUrl}">
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