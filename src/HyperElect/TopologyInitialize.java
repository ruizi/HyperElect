package HyperElect;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TopologyInitialize {
    private int dimension;
    private int nodeNum;
    private ArrayList<Integer> randomValue=new ArrayList<>();

    public TopologyInitialize(int dimension) {
        this.dimension = dimension;
        this.nodeNum = (int) Math.pow(2, dimension);
        System.out.println(dimension);
        System.out.println(nodeNum);
    }

    public int generateRandomNodeValue(){
        int value;
        while(true){
            int rand=new Random().nextInt(nodeNum*5);
            if(!randomValue.contains(rand)){
                randomValue.add(rand);
                value=rand;
                break;
            }
        }
        return value;
    }

    public ArrayList<MyNode> buildingUpNodes() {
        ArrayList<MyNode> nodes = new ArrayList<>();
        String padding = "%" + dimension + "s";
        for (int i = 0; i < nodeNum; i++) {
            String nodeBinaryId = String.format(padding, Integer.toBinaryString(i)).replace(' ', '0');
            int nodeValue = generateRandomNodeValue();
            nodes.add(new MyNode(i, nodeBinaryId,nodeValue));
        }
        return nodes;
    }

    public ArrayList<Link> buildingUpLinks(ArrayList<MyNode> myNodes) {
        ArrayList<Link> links = new ArrayList<>();
        for (int i = 0; i < myNodes.size(); i++) {
            MyNode node1 = myNodes.get(i);
            String nodeBinaryName1 = node1.getNodeBinaryId();
            for (int j = 0; j < myNodes.size(); j++) {
                MyNode node2 = myNodes.get(j);
                String nodeBinaryName2 = node2.getNodeBinaryId();
                int index = 0;
                for (int x = 0; x < nodeBinaryName1.length(); x++) {
                    if (nodeBinaryName1.charAt(x) != nodeBinaryName2.charAt(x)) {
                        index++;
                    }
                    if (index >= 2) {
                        break;
                    }
                }
                if (index == 1) {
                    links.add(new Link(node1, node2));
                }
            }
        }
        return links;
    }
}
