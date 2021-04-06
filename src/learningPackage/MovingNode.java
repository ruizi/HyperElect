package learningPackage;

import io.jbotsim.core.Node;

public class MovingNode extends Node {
    @Override
    public void onStart() {
        super.onStart();
        setDirection(Math.random()*2*Math.PI);
    }

    @Override
    public void onClock() {
        super.onClock();
        move(1);
        wrapLocation();
    }
}
