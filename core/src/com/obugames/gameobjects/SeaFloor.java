package com.obugames.gameobjects;

public class SeaFloor extends Scrollable {

    // When SeaFloor constructor is invoked, invoke the super (Scrollable)
    // constructor
    public SeaFloor(float x, float y, int width, int height, float scrollSpeed) {
        super(x, y, width, height, scrollSpeed);

    }
    
    public void onRestart(float x, float scrollSpeed) {
        position.x = x;
        velocity.x = scrollSpeed;
    }
}
