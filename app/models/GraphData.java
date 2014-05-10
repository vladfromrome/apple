package models;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.algorithm.BetweennessCentrality;
import org.graphstream.algorithm.measure.ClosenessCentrality;
import org.graphstream.algorithm.measure.DegreeCentrality;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * Author: Vladimir Romanov
 * Date: 08.05.14
 * Time: 12:52
 */
public class GraphData {
    Graph graph;
    List<GraphNode> friendNodes;
    List<String> user_ids;

    Integer nodeNum;
    Integer pairsNum;


    /**
     * for a given friendlist computes centrality measures and forms <code>GraphNode</code> objects.
     */
    public GraphData(List<AppFriend> friendlist) {
        this.graph = new SingleGraph("Friends graph");
        this.user_ids = new ArrayList<>();
        this.nodeNum=friendlist.size();
        this.pairsNum=((nodeNum-1)*(nodeNum-2))/2;
        System.out.println("Building computational graph");
        for (AppFriend f : friendlist) { //building nodes and user_ids list
            graph.addNode(f.user_id);
            user_ids.add(f.user_id);
        }
        for (AppFriend a : friendlist) {  //building edges
            for (AppFriend b : a.friends) {
                try {
                    graph.addEdge(a.user_id + "_" + b.user_id, a.user_id, b.user_id);
                } catch (EdgeRejectedException e) {/*ignoring not relevant connecitons*/}
                catch (ElementNotFoundException e){/*ignoring not relevant nodes*/}
            }
        }
        System.out.println("Computing measures");
        this.compute();
        System.out.println("Building GraphNodes");
        this.friendNodes = new ArrayList<>();
        for (AppFriend f : friendlist) {
            List<String> connections = new ArrayList<>();
            for (AppFriend a : f.friends) {
                if (user_ids.contains(a.user_id)){         //ignoring not relevant connecitons
                connections.add(a.user_id);       }
            }
            Node n = graph.getNode(f.user_id);
            Float dc = Float.valueOf(n.getAttribute("dc").toString());
            Float bc = Float.valueOf(n.getAttribute("bc").toString())/Float.valueOf("2");
            Float cc = Float.valueOf(n.getAttribute("cc").toString());
            /*System.out.println(f.name);
            System.out.println(dc);
            System.out.println(bc);
            System.out.println(cc);
            System.out.println(dc_n(dc));
            System.out.println(bc_n(bc));
            System.out.println(cc_n(cc));*/

            friendNodes.add(new GraphNode(
                    connections,
                    f.user_id,
                    f.name,
                    f.nickname,
                    f.gender,
                    f.picId(),
                    dc,
                    bc,
                    cc,
                    dc_n(dc),
                    bc_n(bc),
                    cc_n(cc)
            ));
        }
    }

    private void compute() {
        DegreeCentrality dc = new DegreeCentrality();
        dc.setCentralityAttribute("dc");
        dc.init(graph);
        dc.compute();
        BetweennessCentrality bc = new BetweennessCentrality();
        bc.setCentralityAttributeName("bc");
        bc.computeEdgeCentrality(false);
        bc.init(graph);
        bc.compute();
        ClosenessCentrality cc = new ClosenessCentrality();
        cc.setCentralityAttribute("cc");
        cc.init(graph);
        cc.compute();
    }

    private Float dc_n(Float dc) {
        return dc / (nodeNum - 1);
    }

    private Float bc_n(Float bc) {
        return bc / pairsNum;
    }

    private Float cc_n(Float cc) {
        return (cc * (nodeNum - 1));
    }

    @Override
    public String toString() {
        String s = "";
        for (GraphNode n : friendNodes) {
            s += n.toString() + "\n";
        }

        return s;
    }


}
