package com.pwc.core.framework.data;


public enum CssProperty {

    COLOR("color"),
    OPACITY("opacity"),
    BACKGROUND("background"),
    BACKGROUND_ATTACHMENT("background-attachment"),
    BACKGROUND_BLEND_MODE("background-blend-mode"),
    BACKGROUND_COLOR("background-color"),
    BACKGROUND_IMAGE("background-image"),
    BACKGROUND_POSITION("background-position"),
    BACKGROUND_REPEAT("background-repeat"),
    BACKGROUND_CLIP("background-clip"),
    BACKGROUND_ORIGIN("background-origin"),
    BACKGROUND_SIZE("background-size"),
    BORDER("border"),
    BORDER_BOTTOM("border-bottom"),
    BORDER_BOTTOM_COLOR("border-bottom-color"),
    BORDER_BOTTOM_LEFT_RADIUS("border-bottom-left-radius"),
    BORDER_BOTTOM_RIGHT_RADIUS("border-bottom-right-radius"),
    BORDER_BOTTOM_STYLE("border-bottom-style"),
    BORDER_BOTTOM_WIDTH("border-bottom-width"),
    BORDER_COLOR("border-color"),
    BORDER_IMAGE("border-image"),
    BORDER_IMAGE_OUTSET("border-image-outset"),
    BORDER_IMAGE_REPEAT("border-image-repeat"),
    BORDER_IMAGE_SLICE("border-image-slice"),
    BORDER_IMAGE_SOURCE("border-image-source"),
    BORDER_IMAGE_WIDTH("border-image-width"),
    BORDER_LEFT("border-left"),
    BORDER_LEFT_COLOR("border-left-color"),
    BORDER_LEFT_STYLE("border-left-style"),
    BORDER_LEFT_WIDTH("border-left-width"),
    BORDER_RADIUS("border-radius"),
    BORDER_RIGHT("border-right"),
    BORDER_RIGHT_COLOR("border-right-color"),
    BORDER_RIGHT_STYLE("border-right-style"),
    BORDER_RIGHT_WIDTH("border-right-width"),
    BORDER_STYLE("border-style"),
    BORDER_TOP("border-top"),
    BORDER_TOP_COLOR("border-top-color"),
    BORDER_TOP_LEFT_RADIUS("border-top-left-radius"),
    BORDER_TOP_RIGHT_RADIUS("border-top-right-radius"),
    BORDER_TOP_STYLE("border-top-style"),
    BORDER_TOP_WIDTH("border-top-width"),
    BORDER_WIDTH("border-width"),
    BOX_DECORATION_BREAK("box-decoration-break"),
    BOX_SHADO("box-shadow"),
    BOTTOM("bottom"),
    CLEAR("clear"),
    CLIP("clip"),
    DISPLAY("display"),
    FLOAT("float"),
    HEIGHT("height"),
    LEFT("left"),
    MARGIN("margin"),
    MARGIN_BOTTOM("margin-bottom"),
    MARGIN_LEFT("margin-left"),
    MARGIN_RIGHT("margin-right"),
    MARGIN_TOP("margin-top"),
    MAX_HEIGHT("max-height"),
    MAX_WIDTH("max-width"),
    MIN_HEIGHT("min-height"),
    MIN_WIDTH("min-width"),
    OVERFLOW("overflow"),
    OVERFLOW_X("overflow-x"),
    OVERFLOW_Y("overflow-y"),
    PADDING("padding"),
    PADDING_BOTTOM("padding-bottom"),
    PADDING_LEFT("padding-left"),
    PADDING_RIGHT("padding-right"),
    PADDING_TOP("padding-top"),
    POSITION("position"),
    RIGHT("right"),
    TOP("top"),
    VISIBILITY("visibility"),
    WIDTH("width"),
    VERTICAL_ALIGN("vertical-align"),
    Z_INDEX("z-index");

    public String property;

    private CssProperty(String a) {
        property = a;
    }

}