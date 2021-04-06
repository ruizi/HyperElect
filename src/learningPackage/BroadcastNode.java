package learningPackage;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class BroadcastNode extends Node {

    boolean informed;

    @Override
    public void onStart() {
        super.onStart();
        informed =false;
    }

    @Override
    public void onSelection() {
        super.onSelection();
        informed=true;
        setColor(Color.RED);
        sendAll(new Message(this.getID()));
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
        System.out.println(message);
        //System.out.println(this.getID()+":"+getNeighbors());
        if(!informed){
            informed=true;
            setColor(Color.RED);
            sendAll(new Message(message.getContent()+","+this.getID()));
        }
    }
}
