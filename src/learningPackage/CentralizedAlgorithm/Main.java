package learningPackage.CentralizedAlgorithm;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class Main {
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(LonerMessageBased.class);
        new MyCentralizedAlgorithm(tp);
        new JViewer(tp);
        tp.start();
    }
}
