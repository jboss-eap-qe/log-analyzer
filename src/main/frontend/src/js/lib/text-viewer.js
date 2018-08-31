import $ from 'jquery';

class TextViewer {
    constructor() {
        this.container = $('<div class="text-viewer"></div>');
        this.topGap = null;
        this.bottomGap = null;
        this.fileSize = null;
        this.windowSize = null;
        this.numWindows = null;
        this.containerHeight = $(this.container).height();
        this.windowHeight = null;
        this.loadedWindows = {}
        this.jenkins = null;
        this.project = null;
        this.build = null;
        this.path = null;
    }

    getContainer() {
        return this.container;
    }

    getFileSize() {
        return this.fileSize;
    }

    setFileSize(fileSize) {
        this.fileSize = fileSize;
    }

    getWindowSize() {
        return this.fileSize;
    }

    setWindowSize(windowSize) {
        this.windowSize = windowSize;
    }

    getJenkins() {
        return this.jenkins;
    }

    setJenkins(jenkins) {
        this.jenkins = jenkins;
    }

    getProject() {
        return this.project;
    }

    setProject(project) {
        this.project = project;
    }

    getBuild() {
        return this.build;
    }

    setBuild(build) {
        this.build = build;
    }

    getPath() {
        return this.path;
    }

    setPath(path) {
        this.path = path;
    }

    recalculate() {
        this.containerHeight = $(this.container).height();
    }

    init() {
        this.numWindows = Math.ceil(this.fileSize / this.windowSize);
        this.windowHeight = this.windowSize * this.__getLineHeight();

        this.__handleScroll(0);

        this.container.on('scroll', (e) => {
            this.__handleScroll(e.target.scrollTop);
        })
    }

    __handleScroll(scrollTop) {
        let scrollBottom = scrollTop + this.containerHeight;
        let firstVisibleWindow = Math.floor(scrollTop / this.windowHeight);
        let lastVisibleWindow = Math.floor(scrollBottom / this.windowHeight);

        let visibleLoadedWindows = this.__getLoadedWindowsFromInterval(firstVisibleWindow, lastVisibleWindow);

        if (visibleLoadedWindows.length != 0) {
            this.__removeLoadedUnvisibleWindows(firstVisibleWindow - 1, lastVisibleWindow + 1);
            this.__loadVisibleWindows(Math.max(0, firstVisibleWindow - 1), Math.min(this.numWindows, lastVisibleWindow + 1));
        } else {
            this.loadedWindows = {};
            this.container.empty();
            this.topGap = $('<div class="top-gap"></div>');
            this.container.append(this.topGap);

            for (let i = Math.max(0, firstVisibleWindow - 1); i < Math.min(this.numWindows, lastVisibleWindow + 1); i++) {
                let el = this.__loadWindow(i);
                this.container.append(el);
            }

            this.bottomGap = $('<div class="bottom-gap"></div>');
            this.container.append(this.bottomGap);
        }

        let topGapHeight = firstVisibleWindow * this.windowHeight
        let bottomGapHeight = (this.numWindows - lastVisibleWindow) * this.windowHeight;

        this.topGap.height(topGapHeight);
        this.bottomGap.height(bottomGapHeight);
    }

    __getLineHeight() {
        return 14;
    }

    __getLoadedWindowsFromInterval(first, last) {
        let result = []
        for (let i = first; i <= last; i++) {
            if (this.loadedWindows[i]) {
                result.push(i)
            }
        }
        return result
    }

    __loadWindow(i) {
        let el = $('<pre style="height: ' + this.windowHeight + 'px"></pre>')
        this.loadedWindows[i] = el;
        this.__loadText(el, i);
        return el;
    }

    __loadText(el, i) {

        let data = {
            jenkins: this.jenkins,
            project: this.project,
            build: this.build,
            path: this.path,
            chunk: i
        }

        $.ajax({
            type: 'POST',
            url: '/log-analyzer/rest/js-backend/file/chunk',
            data: JSON.stringify(data),
            processData: false,
            contentType: 'application/json',
            dataType: 'text',
            success: (text) => {
                $(el).html(text);
            }
        });
    }

    __removeLoadedUnvisibleWindows(firstVisible, lastVisible) {
        for (let id in this.loadedWindows) {
            if (id < firstVisible) {
                this.loadedWindows[id].remove();
                let currentHeight = this.topGap.height();
                this.topGap.height(currentHeight + this.windowHeight);
                delete this.loadedWindows[id];
            } else if (id > lastVisible) {
                this.loadedWindows[id].remove();
                let currentHeight = this.bottomGap.height();
                this.bottomGap.height(currentHeight + this.windowHeight);
                delete this.loadedWindows[id];
            }
        }
    }

    __loadVisibleWindows(firstVisible, lastVisible) {
        let loadedWindow = this.__getLoadedWindowsFromInterval(firstVisible, lastVisible)[0]
        let lastLoadedWindowEl = this.loadedWindows[loadedWindow]

        for (let i = loadedWindow - 1; i >= firstVisible; i--) {
            if (this.loadedWindows[i] === undefined) {
                let win = this.__loadWindow(i);
                win.insertBefore(lastLoadedWindowEl);
                lastLoadedWindowEl = win;
            } else {
                lastLoadedWindowEl = this.loadedWindows[i]
            }
        }

        lastLoadedWindowEl = this.loadedWindows[loadedWindow]
        for (let i = loadedWindow + 1; i <= lastVisible; i++) {
            if (this.loadedWindows[i] === undefined) {
                let win = this.__loadWindow(i);
                win.insertAfter(lastLoadedWindowEl);
                lastLoadedWindowEl = win;
            } else {
                lastLoadedWindowEl = this.loadedWindows[i]
            }
        }
    }
}

export default TextViewer;
