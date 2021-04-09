package HyperElect;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyNode extends Node {
    private String state = "ASLEEP"; //initial setting node to be asleep
    private int stage = 0;
    private final String nodeBinaryId;
    private final int nodeValue;
    private final int topologyDimension;
    private ArrayList<Integer> nextDuelist;
    private ArrayList<Message> Delayed = new ArrayList<>();
    private ArrayList<Integer> delay = new ArrayList<>();
    volatile static int messageSent = 0;
    volatile static int stopNodeNum = 0;


    public MyNode(int nodeId, String nodeBinaryId, int nodeValue,int axis_X,int axis_Y) {
        this.setID(nodeId);
        this.nodeBinaryId = nodeBinaryId;
        this.nodeValue = nodeValue;
        this.topologyDimension = nodeBinaryId.length();
        this.setLocation(axis_X,axis_Y);
        this.setColor(Color.black);
    }

    public MyNode(int nodeId, String nodeBinaryId, int nodeValue) {
        this.setID(nodeId);
        this.nodeBinaryId = nodeBinaryId;
        this.nodeValue = nodeValue;
        this.topologyDimension = nodeBinaryId.length();
    }

    public String getNodeBinaryId() {
        return nodeBinaryId;
    }

    @Override
    public int getID() {
        return super.getID();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.stage = 1;
        ArrayList<Integer> Source = new ArrayList<>();
        ArrayList<Integer> Dest = new ArrayList<>();
        Source.add(this.stage);

        MyNode duelistNode = findDuelistAtDimensionOne(); //sending message to dimension one
        send(duelistNode, new Message(new MyMatchingMsg("Match", this.nodeValue, this.stage, Source, Dest)));
        messageSent++;

        this.setColor(Color.WHITE);
        this.state = "DUELIST";
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
        System.out.println(this.getID() + " has New Message:" + message.getContent() + " sent from " + message.getSender());
        switch (this.state) {
            case "ASLEEP":
                ASLEEP(message);
                break;
            case "DUELIST":
                DUELIST(message);
                break;
            case "DEFEATED":
                DEFEATED(message);
                break;
//            case "FOLLOWER":
//                FOLLOWER(message);
        }
    }

    public void ASLEEP(Message message) {
        MyMatchingMsg messageReceived = (MyMatchingMsg) message.getContent();
        this.stage = 1;
        ArrayList<Integer> Dest = new ArrayList<>();
        ArrayList<Integer> Source = new ArrayList<>();
        Source.add(stage);

        MyNode duelistNode = findDuelistAtDimensionOne();
        send(duelistNode, new Message(new MyMatchingMsg("Match", this.nodeValue, this.stage, Source, Dest)));
        messageSent++;

        this.setColor(Color.WHITE);
        this.state = "DUELIST";
        System.out.println(this.getID() + " become a DUELIST!");
        if (messageReceived.getStage() == this.stage) {
            ProcessMessage(messageReceived);
        } else {
            DelayMessage(message);
        }
    }

    public void DUELIST(Message message) {
        MyMatchingMsg messageReceived = (MyMatchingMsg) message.getContent();
        if(messageReceived.getMessage().equals("Notify")){
            ReceivingNotifyMessage(message);
        } else if (this.stage == messageReceived.getStage()) {
            ProcessMessage(messageReceived);
        } else {
            DelayMessage(message);
        }
    }

    private void ReceivingNotifyMessage(Message message) {
        HyperFlood(message);
        this.setColor(Color.ORANGE);
        this.state = "FOLLOWER";
        stopNodeNum++;
        System.out.println(this.getID() + " become a follower!");
        System.out.println("Now " + this.getID() + " ends, the overall messages are " + messageSent + " || stop number: " + stopNodeNum);
    }

    public void DEFEATED(Message message) {
        MyMatchingMsg messageReceived = (MyMatchingMsg) message.getContent();
        if (messageReceived.getMessage().equals("Notify")) { //if receiving the "notify" msg, it means the topology already has a leader
            ReceivingNotifyMessage(message);
        } else {
            if (messageReceived.getDest().isEmpty()) { //if the msg dest
                System.out.println("the nextDueList is " + this.nextDuelist);
                messageReceived.addDest(this.nextDuelist);
            }

            int targetLinkDimension = messageReceived.getDest().get(0);
            messageReceived.removeDestByIndex(0);
            messageReceived.addSource(targetLinkDimension);

            MyNode targetNode = mappingLinkDimensionWithNodeObject(targetLinkDimension);
            send(targetNode, new Message(messageReceived));
            messageSent++;
        }
    }

//    public void FOLLOWER(Message message){
//        MyMatchingMsg messageReceived = (MyMatchingMsg) message.getContent();
//
//        if (messageReceived.getDest().isEmpty()) { //if the msg dest
//            System.out.println("the nextDueList is " + this.nextDuelist);
//            messageReceived.addDest(this.nextDuelist);
//        }
//
//        int targetLinkDimension = messageReceived.getDest().get(0);
//        messageReceived.removeDestByIndex(0);
//        messageReceived.addSource(targetLinkDimension);
//
//        MyNode targetNode = mappingLinkDimensionWithNodeObject(targetLinkDimension);
//        send(targetNode, new Message(messageReceived));
//        messageSent++;
//    }

    public void ProcessMessage(MyMatchingMsg messageReceived) {
        if (messageReceived.getNodeValue() > this.nodeValue) {
            if (this.stage == topologyDimension) {
                sendAll(new Message(new MyMatchingMsg("Notify")));
                messageSent = messageSent + this.getNeighbors().size();
                this.setColor(Color.GREEN);
                this.state = "LEADER";
                System.out.println(this.getID() + " become a LEADER!");
                stopNodeNum++;
                System.out.println("Now " + this.getID() + " ends, the overall messages are " + messageSent + " || stop number: " + stopNodeNum);
            } else {
                stage += 1;
                ArrayList<Integer> source = new ArrayList<>();
                ArrayList<Integer> dest = new ArrayList<>();
                source.add(stage);

                MyNode targetNode = mappingLinkDimensionWithNodeObject(stage);
                send(targetNode, new Message(new MyMatchingMsg("Match", this.nodeValue, this.stage, source, dest)));
                messageSent++;
                CHECK();
            }
        } else {
            System.out.println("Node's value is bigger than the message contains, message is from " + messageReceived.getSource());

            this.nextDuelist = compressSource(messageReceived.getSource()); //Compressed the sequence

            CHECK_ALL();

            this.setColor(Color.RED);
            this.state = "DEFEATED";
            System.out.println(this.getID() + " was DEFEATED!");
        }
    }

    public void DelayMessage(@NotNull Message message) {
        MyMatchingMsg messageReceived = (MyMatchingMsg) message.getContent();
        System.out.println("Stage is not equal, the message will be delay !");
        boolean flagOfAdded = false;
        if (this.Delayed.size() == 0) { //if the delayed message array is empty, simply add the message to the array
            this.Delayed.add(message);
            this.delay.add(messageReceived.getStage());
            flagOfAdded = true;
        } else {
            for (int i = 0; i < this.Delayed.size(); i++) {
                if (messageReceived.getStage() < ((MyMatchingMsg) this.Delayed.get(i).getContent()).getStage()) {
                    this.Delayed.add(i, message);
                    this.delay.add(i, messageReceived.getStage());
                    flagOfAdded = true;
                }
            }
        }
        if (!flagOfAdded) {
            this.Delayed.add(message);
            this.delay.add(messageReceived.getStage());
        }
    }

    public void CHECK() {
        System.out.println("CHECK");
        System.out.println("Delayed size :" + this.Delayed.size());
        System.out.println("Delayed msg :" + this.Delayed);
        if (this.Delayed.size() != 0) {
            int next = this.delay.get(0);
            if (next == this.stage) {
                MyMatchingMsg myMatchingMsg = (MyMatchingMsg) this.Delayed.get(0).getContent();
                this.Delayed.remove(0);
                delay.remove(0);
                ProcessMessage(myMatchingMsg);
            }
        }
    }

    public void CHECK_ALL() {
        System.out.println("CHECK_ALL");
        System.out.println("Delayed size :" + this.Delayed.size());
        System.out.println("Delayed msg :" + this.Delayed);
        while (this.Delayed.size() != 0) {
            Message delayedMessage = this.Delayed.get(0);
            MyMatchingMsg myMatchingMsg = (MyMatchingMsg) delayedMessage.getContent();
            this.Delayed.remove(0);
            delay.remove(0);

            if (myMatchingMsg.getMessage().equals("Notify")) {
                HyperFlood(delayedMessage);
                this.setColor(Color.ORANGE);
                this.state = "FOLLOWER";
                System.out.println(this.getID() + " become a follower!");
                stopNodeNum++;
                System.out.println("Now " + this.getID() + " ends, the overall messages are " + messageSent + " || stop number: " + stopNodeNum);
            } else {
                if (myMatchingMsg.getDest().isEmpty()) {
                    myMatchingMsg.addDest(this.nextDuelist);
                    System.out.println(myMatchingMsg.toString());
                }
                int targetLinkDimension = myMatchingMsg.getDest().get(0);
                myMatchingMsg.removeDestByIndex(0); //Dest = Dest - targetNodeLink
                myMatchingMsg.addSource(targetLinkDimension); //Source = Source + targetNodeLink

                MyNode targetNode = mappingLinkDimensionWithNodeObject(targetLinkDimension);

                send(targetNode, new Message(myMatchingMsg));
                messageSent++;
            }
        }
    }

    public MyNode findDuelistAtDimensionOne() { //Dimension One
        List<Node> neighbors = this.getNeighbors();

        int dimension = this.nodeBinaryId.length();
        String prefix = this.nodeBinaryId.substring(0, dimension - stage);
        String postfix = this.nodeBinaryId.substring(dimension - stage + 1, dimension);
        char x = (char) (this.nodeBinaryId.charAt(dimension - stage) ^ 1);

        String aimNodeBinaryName = prefix + x + postfix;

        int aimNodeId = Integer.parseInt(aimNodeBinaryName, 2);//convert binary to int

        System.out.println("At stage:" + this.stage + " | " + this.nodeBinaryId + " => " + aimNodeBinaryName);

        MyNode targetNode = null;
        for (Node neighbor : neighbors) {
            if (neighbor.getID() == aimNodeId) {
                targetNode = (MyNode) neighbor;
            }
        }
        return targetNode;
    }

    public MyNode mappingLinkDimensionWithNodeObject(int linkDimension) {
        MyNode targetNode = null;
        for (Node node : this.getNeighbors()) {
            MyNode myNode = (MyNode) node;
            String myNodeBinaryName = myNode.getNodeBinaryId();
            if (myNodeBinaryName.charAt(topologyDimension - linkDimension) == this.nodeBinaryId.charAt(topologyDimension - linkDimension)) {
            } else { //reduce the difference and check if the rest is equal or not => in order to ensure only the specific location is difference.
                String temp1 = myNodeBinaryName.substring(0, topologyDimension - linkDimension) + myNodeBinaryName.substring(topologyDimension - linkDimension + 1);
                String temp2 = this.nodeBinaryId.substring(0, topologyDimension - linkDimension) + this.nodeBinaryId.substring(topologyDimension - linkDimension + 1);
                if (temp1.equals(temp2)) {
                    targetNode = myNode;
                    break;
                }
            }
        }
        return targetNode;
    }

    public int calDimension(int nodeId1, int nodeId2) {
        int nodeIdDifference = Math.abs(nodeId1 - nodeId2);
        return (int) ((Math.log(nodeIdDifference) / Math.log(2)) + 1);
    }

    public void HyperFlood(Message message) {
        System.out.println("The node " + this.getID() + " now applying HyperFlood");
        int msgSentInLinkDimension = calDimension(message.getSender().getID(), this.getID()); //get the dimension of the link connecting sender and receiver

        System.out.println("Msg sent from the link with dimension :" + msgSentInLinkDimension);

        for (int i = 0; i < this.getNeighbors().size(); i++) {
            MyNode neighborNode = ((MyNode) this.getNeighbors().get(i)); //convert neighbor to MyNode class
            int neighborLinkDimension = calDimension(this.getID(), neighborNode.getID());
            //int neighborLinkDimension = calDimensionWithBinaryNames(this.nodeBinaryId, neighborNode.getNodeBinaryId()); //cal the the dimension of the link between this node and it's neighbor
            if (neighborLinkDimension > msgSentInLinkDimension) { //if the neighborLinking dimension is higher, then sending the msg.
                send(neighborNode, new Message(message));
                messageSent++;
            }
        }
    }

    public ArrayList<Integer> compressSource(ArrayList<Integer> source) {
        HashMap<Integer, Integer> counter = new HashMap<>();
        for (Integer integer : source) {
            if (counter.containsKey(integer)) {
                counter.put(integer, counter.get(integer) + 1);
            } else {
                counter.put(integer, 1);
            }
        }
        ArrayList<Integer> compressedSequence = new ArrayList<>();
        for (int i : counter.keySet()) {
            if (counter.get(i) % 2 != 0) {
                compressedSequence.add(i);
            }
        }
        System.out.println("Compressed Sequence" + compressedSequence);
        return compressedSequence;
    }


    public String getState() {
        return state;
    }
}
