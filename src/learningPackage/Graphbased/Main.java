package learningPackage.Graphbased;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class Main {
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(LonerGraphBased.class);
        tp.setTimeUnit(500);
        new JViewer(tp);
        tp.start();
    }
}
