import $ from 'jquery';
import WindowManager from 'js/lib/window-manager';
import TextViewer from 'js/lib//text-viewer';

export default () => {
    $('#url-submit').on('click', () => {
        let buildUrl = $('#url-input').val();
        $.ajax({
            url: '/log-analyzer/rest/js-backend/download-and-prepare-logs?url=' + encodeURIComponent(buildUrl),
            success: (resp) => {
                let windowManagerDiv = $('<div id="window-manager"></div>');
                let body = $('body');
                body.empty();
                body.append(windowManagerDiv);
                let windowManager = new WindowManager(windowManagerDiv);

                for (let fileName in resp.files) {
                    let textViewer = new TextViewer();
                    textViewer.setJenkins(resp.jenkins);
                    textViewer.setProject(resp.project);
                    textViewer.setBuild(resp.build);
                    textViewer.setPath(fileName);
                    textViewer.setFileSize(resp.files[fileName].numberOfLines);
                    textViewer.setWindowSize(resp.files[fileName].sizeOfWindow);
                    textViewer.init();
                    windowManager.openWindow(fileName, textViewer.getContainer());
                }
            }
        });
    });
}
