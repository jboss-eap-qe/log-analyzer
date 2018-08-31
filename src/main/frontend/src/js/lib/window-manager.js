import $ from 'jquery';

class Window {

    constructor(container, title, content) {
        this.MIN_WIDTH = 60;
        this.MIN_HEIGHT = 40;
        this.FULLSCREEN_MARGINS = -10;
        this.MARGINS = 4;

        this.container = $(container);
        this.containerOffset = this.container.offset();
        this.title = title;
        this.content = content;
        this.clicked = null;
        this.onRightEdge = null;
        this.onBottomEdge = null;
        this.onLeftEdge = null;
        this.onTopEdge = null;
        this.rightScreenEdge = null;
        this.bottomScreenEdge = null;
        this.preSnapped = null;
        this.b = null;
        this.x = null;
        this.y = null;
        this.redraw = false;
        this.moveEvent = null;

        this.__init();
    }

    __init() {
        this.pane = $('<div class="pane"><div class="title">' + this.title + '</div></div>');
        this.pane.append(this.content);
        this.ghostpane = $('<div class="ghostpane"></div>');
        this.container.append(this.pane);
        this.container.append(this.ghostpane);

        // Mouse events
        this.pane.on('mousedown', $.proxy((e) => { this.__onDown(e); e.preventDefault(); }, this));
        $(window.document).on('mousemove', $.proxy(this.__onMove, this));
        $(window.document).on('mouseup', $.proxy(this.__onUp, this));

        // Touch events
        this.pane.on('touchstart', $.proxy((e) => { this.__onDown(e.touches[0]); e.preventDefault(); }, this));
        $(window.document).on('touchmove', $.proxy((e) => { this.__onMove(e.touches[0]); e.preventDefault(); }, this));
        $(window.document).on('touchend', $.proxy((e) => { if (e.touches.length == 0) onUp(e.changedTouches[0]); }, this));

        this.__animate();
    }

    __onDown(e) {
        this.__calc(e);

        let isResizing = this.onRightEdge || this.onBottomEdge || this.onTopEdge || this.onLeftEdge;

        this.clicked = {
            x: this.x,
            y: this.y,
            cx: e.clientX,
            cy: e.clientY,
            w: this.b.width,
            h: this.b.height,
            isResizing: isResizing,
            isMoving: !isResizing && this.__canMove(),
            onTopEdge: this.onTopEdge,
            onLeftEdge: this.onLeftEdge,
            onRightEdge: this.onRightEdge,
            onBottomEdge: this.onBottomEdge
        };
    }

    __onUp(e) {
        this.__calc(e);

        if (this.clicked && this.clicked.isMoving) {
            // Snap
            var snapped = {
                width: this.b.width,
                height: this.b.height
            };

            if (this.b.top < this.FULLSCREEN_MARGINS || this.b.left < this.FULLSCREEN_MARGINS || this.b.right > this.container.innerWidth() - this.FULLSCREEN_MARGINS || this.b.bottom > this.container.innerHeight() - this.FULLSCREEN_MARGINS) {
                // hintFull();
                this.__setBounds(this.pane, 0, 0, this.container.innerWidth(), this.container.innerWidth());
                this.preSnapped = snapped;
            } else if (this.b.top < this.MARGINS) {
                // hintTop();
                this.__setBounds(this.pane, 0, 0, this.container.innerWidth(), this.container.innerHeight() / 2);
                this.preSnapped = snapped;
            } else if (this.b.left < this.MARGINS) {
                // hintLeft();
                this.__setBounds(this.pane, 0, 0, this.container.innerWidth() / 2, this.container.innerHeight());
                this.preSnapped = snapped;
            } else if (this.b.right > this.rightScreenEdge) {
                // hintRight();
                this.__setBounds(this.pane, this.container.innerWidth() / 2, 0, this.container.innerWidth() / 2, this.container.innerHeight());
                this.preSnapped = snapped;
            } else if (this.b.bottom > this.bottomScreenEdge) {
                // hintBottom();
                this.__setBounds(this.pane, 0, this.container.innerHeight() / 2, this.container.innerWidth(), this.container.innerWidth() / 2);
                this.preSnapped = snapped;
            } else {
                this.preSnapped = null;
            }

            this.__hintHide();
            this.clicked = null;

        }
        this.clicked = null;
    }

    __onMove(e) {

        this.__calc(e);
        this.moveEvent = e;
        this.redraw = true;
    }

    __canMove() {
        return this.x > 0 && this.x < this.b.width && this.y > 0 && this.y < this.b.height && this.y < 30;
    }

    __calc(e) {
        this.b = this.pane.position();
        this.b.width = this.pane.width();
        this.b.height = this.pane.height();
        this.b.bottom = this.b.top + this.b.height;
        this.b.right = this.b.left + this.b.width;
        this.x = e.clientX - this.b.left - this.containerOffset.left;
        this.y = e.clientY - this.b.top - this.containerOffset.top;

        this.onTopEdge = this.y < this.MARGINS;
        this.onLeftEdge = this.x < this.MARGINS;
        this.onRightEdge = this.x >= this.b.width - this.MARGINS;
        this.onBottomEdge = this.y >= this.b.height - this.MARGINS;

        this.rightScreenEdge = this.container.innerWidth() - this.MARGINS;
        this.bottomScreenEdge = this.container.innerHeight() - this.MARGINS;
    }

    __animate() {

        window.requestAnimationFrame($.proxy(this.__animate, this));

        if (!this.redraw) return;

        this.redraw = false;

        if (this.clicked && this.clicked.isResizing) {

            if (this.clicked.onRightEdge) this.pane.css('width', Math.max(this.x, this.MIN_WIDTH));
            if (this.clicked.onBottomEdge) this.pane.css('height', Math.max(this.y, this.MIN_HEIGHT));

            if (this.clicked.onLeftEdge) {
                let currentWidth = Math.max(this.clicked.cx - this.moveEvent.clientX  + this.clicked.w, this.MIN_WIDTH);
                if (currentWidth > this.MIN_WIDTH) {
                    this.pane.css('width', currentWidth);
                    this.pane.css('left', this.moveEvent.clientX - this.containerOffset.left);
                }
            }

            if (this.clicked.onTopEdge) {
                let currentHeight = Math.max(this.clicked.cy - this.moveEvent.clientY  + this.clicked.h, this.MIN_HEIGHT);
                if (currentHeight > this.MIN_HEIGHT) {
                    this.pane.css('height', currentHeight);
                    this.pane.css('top', this.moveEvent.clientY - this.containerOffset.top);
                }
            }

            this.__hintHide();

            return;
        }

        if (this.clicked && this.clicked.isMoving) {

            if (this.b.top < this.FULLSCREEN_MARGINS || this.b.left < this.FULLSCREEN_MARGINS || this.b.right > this.container.innerWidth() - this.FULLSCREEN_MARGINS || this.b.bottom > this.container.innerHeight() - this.FULLSCREEN_MARGINS) {
                // hintFull();
                this.__setBounds(this.ghostpane, 0, 0, this.container.innerWidth(), this.container.innerHeight());
                this.ghostpane.css('opacity', 0.2);
            } else if (this.b.top < this.MARGINS) {
                // hintTop();
                this.__setBounds(this.ghostpane, 0, 0, this.container.innerWidth(), this.container.innerHeight() / 2);
                this.ghostpane.css('opacity', 0.2);
            } else if (this.b.left < this.MARGINS) {
                // hintLeft();
                this.__setBounds(this.ghostpane, 0, 0, this.container.innerWidth() / 2, this.container.innerHeight());
                this.ghostpane.css('opacity', 0.2);
            } else if (this.b.right > this.rightScreenEdge) {
                // hintRight();
                this.__setBounds(this.ghostpane, this.container.innerWidth() / 2, 0, this.container.innerWidth() / 2, this.container.innerHeight());
                this.ghostpane.css('opacity', 0.2);
            } else if (this.b.bottom > this.bottomScreenEdge) {
                // hintBottom();
                this.__setBounds(this.ghostpane, 0, this.container.innerHeight() / 2, this.container.innerWidth(), this.container.innerWidth() / 2);
                this.ghostpane.css('opacity', 0.2);
            } else {
                this.__hintHide();
            }

            if (this.preSnapped) {
                this.__setBounds(this.pane,
                    this.moveEvent.clientX - this.preSnapped.width / 2,
                    this.moveEvent.clientY - Math.min(this.clicked.y + this.containerOffset.top, this.preSnapped.height),
                    this.preSnapped.width,
                    this.preSnapped.height
                );
                return;
            }

            // moving

            this.pane.css('top', this.moveEvent.clientY - this.clicked.y - this.containerOffset.top);
            this.pane.css('left', this.moveEvent.clientX - this.clicked.x - this.containerOffset.left);

            return;
        }

        // This code executes when mouse moves without clicking

        // style cursor
        if (this.onRightEdge && this.onBottomEdge || this.onLeftEdge && this.onTopEdge) {
            this.pane.css('cursor', 'nwse-resize');
        } else if (this.onRightEdge && this.onTopEdge || this.onBottomEdge && this.onLeftEdge) {
            this.pane.css('cursor', 'nesw-resize');
        } else if (this.onRightEdge || this.onLeftEdge) {
            this.pane.css('cursor', 'ew-resize');
        } else if (this.onBottomEdge || this.onTopEdge) {
            this.pane.css('cursor', 'ns-resize');
        } else if (this.__canMove()) {
            this.pane.css('cursor', 'move');
        } else {
            this.pane.css('cursor', 'default');
        }
    }

    __hintHide() {
      this.__setBounds(this.ghostpane, this.b.left, this.b.top, this.b.width, this.b.height);
      this.ghostpane.css('opacity', 0);
    }

    __setBounds(element, x, y, w, h) {
    	element.css('left', x);
        element.css('top', y);
        element.css('width', w);
        element.css('height', h);
    }
}

class WindowManager {

    constructor(container) {
        this.container = container;
        this.windows = [];
    }

    openWindow(title, content) {
        let win = new Window(this.container, title, content);
        this.windows.push(win);
    }
}

export default WindowManager;
