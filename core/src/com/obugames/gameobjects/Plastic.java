package com.obugames.gameobjects;

import java.util.Random;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;


public class Plastic extends Scrollable {

    private Random r;
    private float groundY;
    private Circle boundingCircle;
    private boolean isScored = false;

    // When Plastic constructor is invoked, invoke the super (Scrollable)
    // constructor
    public Plastic(float x, float y, int width, int height, float scrollSpeed, float groundY) {
        super(x, y, width, height, scrollSpeed);
        // Initialize a Random object for Random number generation
        r = new Random();
        boundingCircle = new Circle();

        this.groundY = groundY;
    }

    @Override
    public void update(float delta) {
        // Call the update method in the superclass (Scrollable)
        super.update(delta);

     // Set the circle's center to be (9, 6) with respect to the bird.
        // Set the circle's radius to be 6.5f;
        boundingCircle.set(position.x + 10, position.y + 9, 10.5f);
    }
    
    @Override
    public void reset(float newX) {
        // Call the reset method in the superclass (Scrollable)
        super.reset(newX);
        // Change the height to a random number
        //height = r.nextInt(90) + 15;
        position.y = r.nextInt(90) + 15;
        isScored = false;
    }
    
    public boolean collides(Shark shark) {
        if (position.x < shark.getX() + shark.getWidth()) {
            return (Intersector.overlaps(shark.getBoundingCircle(), boundingCircle));
        }
        return false;
    }

    public Circle getBoundingCircle() {
        return boundingCircle;
    }
    
    public boolean isScored() {
        return isScored;
    }

    public void setScored(boolean b) {
        isScored = b;
    }
    
    public void onRestart(float x, float scrollSpeed) {
        velocity.x = scrollSpeed;
        reset(x);
    }
}