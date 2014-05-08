package controllers;
import models.fbtest.AppFriend;
import models.fbtest.GraphData;
import org.graphstream.algorithm.BetweennessCentrality;
import org.graphstream.algorithm.measure.ClosenessCentrality;
import org.graphstream.algorithm.measure.DegreeCentrality;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.*;
import org.graphstream.graph.Node;

import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Author: Vladimir Romanov
 * Date: 07.05.14
 * Time: 10:34
 */
public class GraphController {
    public static void main(String args[]) {

        GraphData graphData = new GraphData(modelA());
        System.out.println("GraphData is ready!");
        System.out.println(graphData.toString());
    }

    private static List<AppFriend> modelA(){

        AppFriend a = new AppFriend("11","Alan","n","g");
        AppFriend b = new AppFriend("12","Bob","n","g");
        AppFriend c = new AppFriend("13","Clara","n","g");
        AppFriend d = new AppFriend("14","Dan","n","g");
        AppFriend e = new AppFriend("15","Eva","n","g");

        a.friends.add(b);
        b.friends.add(a);
        b.friends.add(c);
        c.friends.add(b);
        c.friends.add(d);
        d.friends.add(c);
        d.friends.add(e);
        e.friends.add(d);

        List<AppFriend> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);

        return list;
    }

}
