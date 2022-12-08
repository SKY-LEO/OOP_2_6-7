package com.example.patternjavafx.Controllers;

public class ControllerAndPane {
    private Object controller;
    private String pane;

    private int width;
    private int height;

    public ControllerAndPane(Object controller, String pane, int width, int height) {
        this.controller = controller;
        this.pane = pane;
        this.width = width;
        this.height = height;
    }

    public Object getController() {
        return controller;
    }

    public String getPane() {
        return pane;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
