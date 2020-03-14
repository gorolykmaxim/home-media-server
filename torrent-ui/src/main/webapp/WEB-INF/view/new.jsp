<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Download torrents</title>
        <app:header></app:header>
    </head>
    <body class="app-background-color">
        <app:navigation></app:navigation>
        <div class="mdc-dialog mdc-dialog--open">
            <div class="mdc-dialog__container">
                <div class="mdc-dialog__surface">
                    <form method="post" action="${submitUrl}">
                        <h2 class="mdc-dialog__title">Download torrent</h2>
                        <div class="mdc-dialog__content">
                            <div class="app-form-field">
                                <div class="mdc-text-field mdc-text-field--fullwidth mdc-text-field--no-label mdc-text-field--textarea" data-mdc-auto-init="MDCTextField">
                                    <textarea id="magnet-link" name="magnetLink" class="mdc-text-field__input"></textarea>
                                    <div class="mdc-notched-outline">
                                        <div class="mdc-notched-outline__leading"></div>
                                        <div class="mdc-notched-outline__notch">
                                            <label class="mdc-floating-label" for="magnet-link">Magnet link</label>
                                        </div>
                                        <div class="mdc-notched-outline__trailing"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="app-form-field">
                                <div class="mdc-text-field" data-mdc-auto-init="MDCTextField">
                                    <input class="mdc-text-field__input" id="download-folder" name="downloadFolder" value="${defaultDownloadFolder}"/>
                                    <div class="mdc-line-ripple"></div>
                                    <label for="download-folder" class="mdc-floating-label">Download folder</label>
                                </div>
                            </div>
                        </div>
                        <footer class="mdc-dialog__actions">
                            <button type="submit" class="mdc-button mdc-button--raised mdc-dialog__button">
                                <div class="mdc-button__ripple"></div>
                                Download
                            </button>
                            <a class="mdc-button mdc-dialog__button" href="/">
                                <div class="mdc-button__ripple"></div>
                                <span class="mdc-button__label">Back</span>
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