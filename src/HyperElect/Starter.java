package HyperElect;

import io.jbotsim.core.Link;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Starter {
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(MyNode.class);
        System.out.println("Please input the dimension of the hypercube?");
        Scanner scanner = new Scanner(System.in);
        int dimension = scanner.nextInt();
        TopologyInitialize newHypercube = new TopologyInitialize(dimension);
        ArrayList<MyNode> myNodes = newHypercube.buildingUpNodes();
        System.out.println(Arrays.toString(myNodes.toArray()));
        ArrayList<Link> links = newHypercube.buildingUpLinks(myNodes);
        System.out.println(Arrays.toString(links.toArray()));


        for (int i = 0; i < myNodes.size(); i++) {
            tp.addNode(myNodes.get(i));
        }
        for (int i = 0; i < links.size(); i++) {
            tp.addLink(links.get(i));
        }


        tp.disableWireless();
        new JViewer(tp);

        tp.start();
        //System.out.println("Message sum: "+myNodes.get(0).getMessageSent());
    }
}
