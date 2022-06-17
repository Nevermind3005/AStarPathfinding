package com.nevermind.astarpathfinding;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;

public class Input {

    private Scene scene;
    public static ArrayList<KeyCode> keys = new ArrayList<>();

    public Input(Scene scene) {
        this.scene = scene;

        scene.setOnKeyReleased(event -> {
            keys.remove(event.getCode());
        });

        scene.setOnKeyPressed(event -> {
            if (!keys.contains(event.getCode())) {
                keys.add(event.getCode());
            }
        });
    }




}
