package utilities;
import models.AppFriend;
import models.GraphData;

import java.util.ArrayList;
import java.util.List;


/**
 * Author: Vladimir Romanov
 * Date: 07.05.14
 * Time: 10:34
 */
public class GraphManager {
    public static void main(String args[]) {

        GraphData graphData = new GraphData(modelA());
        System.out.println("GraphData is ready!");
        System.out.println(graphData.toString());
    }

    /**
     * Line model
     *  A-B-C-D-E
     */
    private static List<AppFriend> modelA(){
        AppFriend a = new AppFriend("11","Alan","n","g");
        AppFriend b = new AppFriend("12","Bob","n","g");
        AppFriend c = new AppFriend("13","Clara","n","g");
        AppFriend d = new AppFriend("14","Dan","n","g");
        AppFriend e = new AppFriend("15","Eva","n","g");
        //AppFriend f = new AppFriend("16","Fred","n","g"); //to check that pointers to friends out of scope will be ignored.

        a.friends.add(b);
        b.friends.add(a);
        b.friends.add(c);
        c.friends.add(b);
        c.friends.add(d);
        d.friends.add(c);
        d.friends.add(e);
        e.friends.add(d);
        //e.friends.add(f);

        List<AppFriend> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);

        return list;
    }

    /**
     * Butterfly model
     * A       F
     * |\     /|
     * | C-D-E |
     * |/     \|
     * B       G
     */
    private static List<AppFriend> modelB(){

        AppFriend a = new AppFriend("11","Alan","n","g");
        AppFriend b = new AppFriend("12","Bob","n","g");
        AppFriend c = new AppFriend("13","Clara","n","g");
        AppFriend d = new AppFriend("14","Dan","n","g");
        AppFriend e = new AppFriend("15","Eva","n","g");
        AppFriend f = new AppFriend("16","Fred","n","g");
        AppFriend g = new AppFriend("17","George","n","g");

        a.friends.add(b);
        a.friends.add(c);
        b.friends.add(a);
        b.friends.add(c);
        c.friends.add(a);
        c.friends.add(b);
        c.friends.add(d);
        d.friends.add(c);
        d.friends.add(e);
        e.friends.add(d);
        e.friends.add(f);
        e.friends.add(g);
        f.friends.add(e);
        f.friends.add(g);
        g.friends.add(e);
        g.friends.add(f);

        List<AppFriend> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);
        list.add(f);
        list.add(g);

        return list;
    }


}
