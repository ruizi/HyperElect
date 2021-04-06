package learningPackage.Graphbased;

import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;

import java.util.List;

public class LonerGraphBased extends Node {
    @Override
    public void onStart() {
        super.onStart();
        if(getNeighbors().isEmpty()){
            setColor(Color.green);
        }else {
            setColor(Color.red);
        }
    }

    @Override
    public void onLinkAdded(Link link) {
        System.out.println("Now thread on node "+this.getID());
        super.onLinkAdded(link);
        setColor(Color.red);
        System.out.println(this.getID()+" Has "+this.getLinks());
        List<Node> neighbors = this.getNeighbors();
        System.out.println("the link to the first neighbor"+this.getCommonLinkWith(neighbors.get(neighbors.size()-1)));
    }

    @Override
    public void onLinkRemoved(Link link) {
        super.onLinkRemoved(link);
        if(getNeighbors().isEmpty()){
            setColor(Color.green);
        }
    }
}
