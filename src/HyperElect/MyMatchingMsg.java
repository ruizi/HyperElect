package HyperElect;

import java.util.ArrayList;

public class MyMatchingMsg {
    private String message = "Match";
    private int nodeValue;
    private int stage;
    private ArrayList<Integer> Source;
    private ArrayList<Integer> Dest;

    public MyMatchingMsg(String message) {
        this.message = message;
    }

    public MyMatchingMsg(String message, int nodeValue, int stage, ArrayList<Integer> Source, ArrayList<Integer> dest) {
        this.message = message;
        this.nodeValue = nodeValue;
        this.stage = stage;
        this.Source = Source;
        this.Dest = dest;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(int nodeValue) {
        this.nodeValue = nodeValue;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public ArrayList<Integer> getSource() {
        return Source;
    }

    public void setSource(ArrayList<Integer> source) {
        Source = source;
    }

    public ArrayList<Integer> getDest() {
        return Dest;
    }

    public void setDest(ArrayList<Integer> dest) {
        Dest = dest;
    }
    public void addDest(ArrayList<Integer> dest){
        this.Dest.addAll(dest);
    }

    public void removeDestByIndex(int index){
        this.Dest.remove(index);
    }

    public void addSource(int newSource){
        this.Source.add(newSource);
    }

//    public Node getSender(){
//        return this.getSender();
//    }

    @Override
    public String toString() {
        return "MyMatchingMsg{" +
                "message='" + message + '\'' +
                ", nodeValue=" + nodeValue +
                ", stage=" + stage +
                ", Source=" + Source +
                ", Dest=" + Dest +
                '}';
    }
}
