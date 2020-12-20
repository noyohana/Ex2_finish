package api;

import com.google.gson.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/** a class of algorithms for the DWGraph_DS class.
 * 
 */
public class DWGraphs_Algo implements dw_graph_algorithms {

    private DWGraph_DS graph;                   


    /** empty constructor.
     *  
     */
    public DWGraphs_Algo(){}

    /** basic constructor, sets the graph as inner graph */
    
    public DWGraphs_Algo(directed_weighted_graph g){
        graph = (DWGraph_DS) g;
    }

    /** set g as the new graph */
    @Override
    public void init(directed_weighted_graph g) {
        graph = (DWGraph_DS) g;
    }

    /** return the current graph */
    @Override
    public directed_weighted_graph getGraph() {
        return graph;
    }

    /** returns a copy of the graph as an object object,
     * instead of a shallow pointer.
     * @return copy of the graph as an object of directed_weighted_graph.
     */
    @Override
    public directed_weighted_graph copy() {
        DWGraph_DS newGraph = new DWGraph_DS();
        Collection<node_data> nodes = graph.getV();                       
        for(node_data node : nodes){                                       
            newGraph.addNode(new NodeData(node.getKey()));
        }
        for(node_data node: nodes){                                       
            Collection<edge_data> edges = graph.getE(node.getKey());
            for(edge_data e : edges){                                     
                newGraph.connect(e.getSrc(), e.getDest(), e.getWeight());
            }
        }
        return newGraph;
    }

    /** cast a directed_weighted_graph object to DWGraph_DS
     *  by making a copy of him of the DWGraph type.
     * @param oldG the graph to be copied
     * @return DWGraph object with equals elements as g
     */
    public DWGraph_DS caster(directed_weighted_graph oldG){
        DWGraph_DS newGr = new DWGraph_DS();
        Collection<node_data> nodes = oldG.getV();                     
        for(node_data node : nodes){                                   
            newGr.addNode(new NodeData(node.getKey()));
        }
        for(node_data node: nodes){                                    
            Collection<edge_data> edges = oldG.getE(node.getKey());
            for(edge_data e : edges){                                   
                newGr.connect(e.getSrc(), e.getDest(), e.getWeight());
            }
        }
        return newGr;
    }

    /** checks if every node is connected to every other node.
     * utilizes Kosoraju algorithm.
     * @return if the graph is strongly connected
     */
    @Override
    public boolean isConnected() {
        clearTag();                                             
        LinkedList<node_data> queu = new LinkedList<>();           
        List<node_data> nodes = (List<node_data>) graph.getV();
        if(nodes.size() == 0 || nodes.size() == 1){              
            return true;
        }
        node_data src = nodes.get(0);   //random starting node
        queu.add(src);
        while(!queu.isEmpty()){            //run until checked every node
            src = queu.remove();
            Collection<edge_data> edges = graph.getE(src.getKey());
            for(edge_data e : edges){                                   //check every edge pointing from the node
                node_data node = graph.getNode(e.getDest());
                if(node.getTag() == 0){                                 //check if the new node is visited
                    queu.add(node);
                }
                node.setTag(1);                                         //tag as visited
            }
        }
        for(node_data node1 : nodes){        //check if there was an unvisited node
            if(node1.getTag() == 0){         //if there is then we cant reach him
                return false;
            }
        }
        clearTag();                          //reset the tags and repeat the function only when the edges are reversed
        queu = new LinkedList<node_data>();
        src = nodes.get(0);
        queu.add(src);
        while(!queu.isEmpty()){
            src = queu.remove();
            Collection<edge_data> edges = graph.getBE(src.getKey());   //check every edge pointing to node
            for(edge_data e : edges){
                node_data node = graph.getNode(e.getSrc());
                if(node.getTag() == 0){
                    queu.add(node);
                }
                node.setTag(1);
            }
        }
        for(node_data node1 : nodes){
            if(node1.getTag() == 0){
                return false;
            }
        }
        return true;
    }

    /** calculates the shortest distance from source node to
     *  
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if(src == dest){
            return 0;
        }
        ComparatorNode comparator = new ComparatorNode(); 
        this.clearWeight();        
        node_data node, nDest;
        node = graph.getNode(src);
        nDest = graph.getNode(dest);
        if(node != null && nDest != null){   
            node.setInfo("");             
            PriorityQueue<node_data> que = new PriorityQueue<>(graph.nodeSize(), comparator); 
            que.add(node);
            while(!que.isEmpty()){           
                node = que.remove();
                if(node.getKey() == dest){    
                    return node.getWeight();  
                }
                Collection<edge_data> edges = graph.getE(node.getKey());
                for(edge_data e : edges){                              
                    node_data temp = graph.getNode(e.getDest());
                    double dist = node.getWeight() + e.getWeight();  
                    
                    if((dist < temp.getWeight() || temp.getWeight() == 0 )&& temp.getKey() != src){
                        if(dist < temp.getWeight()){
                            que.remove(temp);     
                        }
                        temp.setInfo(node.getInfo() + temp.getKey() + ",");
                        temp.setWeight(dist);                               
                        que.add(temp);                                      
                    }
                }
            }
        }
        return -1;
    }

    /** calculate the shortest path from src node to dest node.
     * @param src - start node
     * @param dest - end (target) node
     * @return a list of nodes in the path from src to dest.
     * @return null if there is bo path.
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if(this.shortestPathDist(src, dest) != -1){           
            LinkedList<node_data> pathList = new LinkedList<>();
            node_data node = graph.getNode(src);              
            pathList.add(node);
            if(src == dest){ 
                return pathList;
            }
            String info = graph.getNode(dest).getInfo();      
            while(!info.isEmpty()){                           
                int divider = info.indexOf(",");              
                String key = info.substring(0,divider);        
                node = graph.getNode(Integer.parseInt(key));
                pathList.add(node);                                
                info = info.substring(divider+1);
            }
            return pathList;
        }
        return null;
    }

    /** save the graph to a Json file.
     * @param file - the file name (may include a relative path).
     * @return if the save was successful.
     */
    @Override
    public boolean save(String file) {
        JsonObject jObject = new JsonObject();
        JsonObject jEdge;
        JsonObject jNode;
        JsonArray edges = new JsonArray();
        JsonArray nodes = new JsonArray();
        Collection<node_data> vertice = graph.getV();
        for(node_data node : vertice){                  
            jNode = new JsonObject();
            geo_location GPosition = node.getLocation();             
            String pos = GPosition.x()+","+ GPosition.y()+","+ GPosition.z();
            jNode.addProperty("pos",pos);
            jNode.addProperty("id",node.getKey());     
            nodes.add(jNode);
            Collection<edge_data> EdegeCollect = graph.getE(node.getKey());
            for(edge_data edge : EdegeCollect){                               
                jEdge = new JsonObject();
                jEdge.addProperty("src", edge.getSrc());   
                jEdge.addProperty("w", edge.getWeight());
                jEdge.addProperty("dest", edge.getDest());
                edges.add(jEdge);
            }
        }
        jObject.add("Edges",edges);   
        jObject.add("Nodes",nodes);
        try{
            FileWriter f = new FileWriter(file);   
            f.write(jObject.toString());
            f.close();
            return true;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    /** load a graph from a json file.
     * @param file - file name of JSON file
     * @return if the load was successful.
     */
    @Override
    public boolean load(String file) {
        try{
            String jstr = new String(Files.readAllBytes(Paths.get(file)));   //read the file
            graph = (DWGraph_DS) Json2Graph(jstr); //set the graph
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /** convert a json string to a graph.
     *  is based on the load() method.
     * @param Jstr
     * @return the graph made from the Json string
     */
    public directed_weighted_graph Json2Graph(String Jstr){
        DWGraph_DS newGr = new DWGraph_DS();
        JsonObject json_obj;
        json_obj = JsonParser.parseString(Jstr).getAsJsonObject();
        JsonArray Jnodes = json_obj.getAsJsonArray("Nodes");  //convert the json array to nodes
        for(JsonElement node : Jnodes){
            JsonObject temp = (JsonObject) node;
            int id = temp.get("id").getAsInt();
            NodeData newNode = new NodeData(id);        //create the ne node by the id
            String pos = temp.get("pos").getAsString(); //convert the position json string to a GeoLocation
            int firstComma = pos.indexOf(",");
            int lastComma = pos.lastIndexOf(",");
            double x = Double.parseDouble(pos.substring(0,firstComma));
            double y = Double.parseDouble(pos.substring(firstComma+1,lastComma));
            double z = Double.parseDouble(pos.substring(lastComma+1));
            GeoLocation GL = new GeoLocation(x,y,z);
            newNode.setLocation(GL);   //set the new position
            newGr.addNode(newNode);    //add the node
        }
        JsonArray Jedges = json_obj.getAsJsonArray("Edges");  //convert the edges json array to edges
        for(JsonElement edge : Jedges){
            JsonObject temp = (JsonObject) edge;         //extract the data of the edge json object
            int src = temp.get("src").getAsInt();
            int dest = temp.get("dest").getAsInt();
            double weight = temp.get("w").getAsDouble();
            newGr.connect(src,dest,weight);    //add the edges
        }
        return newGr;
    }

    //resets all the nodes' tag. mainly for algorithmic use
    private void clearTag(){
        Collection<node_data> nodes = graph.getV();
        for(node_data node : nodes){
            node.setTag(0);
        }
    }

    //resets all the nodes' weight. mainly for algorithmic use
    private void clearWeight(){
        Collection<node_data> nodes = graph.getV();
        for(node_data node : nodes){
            node.setWeight(0);
        }
    }
   
    private class ComparatorNode implements Comparator<node_data> {

        @Override
        public int compare(node_data o1, node_data o2) {
            if((o1.getWeight() - o2.getWeight()) > 0){ return 1;}
            else if((o1.getWeight() - o2.getWeight()) < 0){ return -1;}
            else { return 0;}
        }
    }
}
