package HyperElect;

import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Starter {
    public static void main(String[] args) {


        int messageSum = 0;
        System.out.println("Please input how many time do you want to run?");
        Scanner scanner = new Scanner(System.in);
        int runTimes = scanner.nextInt();
        System.out.println("Please input the dimension of the hypercube?");
        scanner = new Scanner(System.in);
        int dimension = scanner.nextInt();

        for (int x = 0; x < runTimes; x++) {
            Topology tp = new Topology();
            tp.setDefaultNodeModel(MyNode.class);
            if(runTimes==1){
                Node sleepNode = new Node();
                Node duelistNode = new Node();
                Node defeatedNode = new Node();
                Node followerNode = new Node();
                Node leaderNode = new Node();
                sleepNode.setColor(Color.black); sleepNode.setLocation(230,20); sleepNode.setLabel("Sleep");
                duelistNode.setColor(Color.WHITE); duelistNode.setLocation(330,20);duelistNode.setLabel("Duelist");
                defeatedNode.setColor(Color.RED);defeatedNode.setLocation(430,20);defeatedNode.setLabel("Defeated");
                followerNode.setColor(Color.ORANGE);followerNode.setLocation(530,20);followerNode.setLabel("Follower");
                leaderNode.setColor(Color.GREEN);leaderNode.setLocation(630,20);leaderNode.setLabel("Leader");
                tp.addNode(sleepNode);
                tp.addNode(duelistNode);
                tp.addNode(defeatedNode);
                tp.addNode(followerNode);
                tp.addNode(leaderNode);
                tp.setTimeUnit(1000);
            }

            TopologyInitialize newHypercube = new TopologyInitialize(dimension);
            ArrayList<MyNode> myNodes = newHypercube.buildingUpNodes();
            System.out.println(Arrays.toString(myNodes.toArray()));
            ArrayList<Link> links = newHypercube.buildingUpLinks(myNodes);
            System.out.println(Arrays.toString(links.toArray()));


            for (int i = 0; i < myNodes.size(); i++) {
                //myNodes.get(i).setLocation(100,100);
                tp.addNode(myNodes.get(i));
            }
            for (int i = 0; i < links.size(); i++) {
                tp.addLink(links.get(i));
            }


            tp.disableWireless();

            JViewer jViewer = new JViewer(tp);
            jViewer.setTitle("HyperElect : Black: Sleep; White: Duelist; Red: Defeated; Orange: Follower; Green: Leader");
            jViewer.setSize(800,500);
            tp.start();

            while (MyNode.stopNodeNum != myNodes.size()) {
                ;
            }
            System.out.println("Stopped");
            if(runTimes!=1){
                tp.clear();
            }
            myNodes.clear();

            MyNode.stopNodeNum = 0;

            messageSum += MyNode.messageSent;

            MyNode.messageSent = 0;
            //System.out.println("Message sum: "+myNodes.get(0).getMessageSent());

        }

        System.out.println("After running "+runTimes+" times of HyperElect in the "+dimension+"-dimension hypercube, the average message is "+messageSum/runTimes);


    }
}
