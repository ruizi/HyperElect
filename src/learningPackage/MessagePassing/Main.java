package learningPackage.MessagePassing;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(LonerMessageBased.class);
        tp.setTimeUnit(500);
        new JViewer(tp);
        //tp.start();
        String x="12345";
        String substring = x.substring(0,3);
        String substring1 = x.substring(3, 5);
        System.out.println(substring);
        System.out.println(substring1);

        ArrayList<Integer> arrayList = new ArrayList<>();

        arrayList.add(3);
        arrayList.add(4);
        arrayList.add(6);
        arrayList.add(7);
        arrayList.add(2,5);
        System.out.println(Arrays.toString(arrayList.toArray()));
        arrayList.add(5,8);
        System.out.println(Arrays.toString(arrayList.toArray()));

        //System.out.println(Math.log());
        System.out.println(Math.log(Math.exp(1)));

        ArrayList<Integer> source = new ArrayList<>();
        source.add(1);
        source.add(2);
        source.add(4);
        source.add(2);
        source.add(1);
        source.add(2);

    }
}
