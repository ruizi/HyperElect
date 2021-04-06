package learningPackage;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class EmptyNode extends Node {
    @Override
    public void onStart() {
        super.onStart();
        // JBotSim executes this method on each node upon initialization
    }

    @Override
    public void onSelection() {
        super.onSelection();
        // JBotSim executes this method on a selected node
    }

    @Override
    public void onClock() {
        super.onClock();
        // JBotSim executes this method on each node in each round
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
        // JBotSim executes this method on a node every time it receives a message
    }
}
