package api;
//finish 

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/** a class that represents a directional weighted graph.
 *  build from the NodeData and edge_data classes.
 */
public class DWGraph_DS implements directed_weighted_graph {

    private HashMap<Integer, node_data> nodes = new HashMap<>();
    private LinkedList<node_data> vertices = new LinkedList<>();       
    private int edgeCounter = 0;   //edge counter
    private int mC = 0;   
    /**
     * @param key - the node_id
     * @return the node by it's id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if (nodes.containsKey(key)) { 
            return nodes.get(key);
        }
        return null;
    }

    /**
     * @param src the source node of the edge.
     * @param dest the destination of the edge
     * @return the requested edge, null if none.
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if (nodes.containsKey(src) && nodes.containsKey(dest) && src != dest) {  
            NodeData node = (NodeData) nodes.get(src);
            return node.getEdge(dest);
        }
        return null;
    }

    /** add a new node to the graph.
     * does nothing if the node already exist.
     * @param n the node object
     */
    @Override
    public void addNode(node_data n) {
        if (!nodes.containsKey(n.getKey())) { 
            nodes.put(n.getKey(), n);         
            vertices.add(n);
            mC++;
        }
    }

  
    @Override
    public void connect(int src, int dest, double w) {
        if (nodes.containsKey(src) && nodes.containsKey(dest) && w > 0 && src != dest) { 
            NodeData srcNode = (NodeData) nodes.get(src);
            NodeData destNode = (NodeData) nodes.get(dest);
            if (!srcNode.hasEdge(dest)) { 
                EdgeData edge = new EdgeData(w, nodes.get(src), nodes.get(dest)); 
                srcNode.addEdge(edge);  
                destNode.backEdge(edge); 
                edgeCounter++;
                mC++;
            }
        }
    }

    /** @return a collection of all the nodes. */
    @Override
    public Collection<node_data> getV() {
        return vertices;
    }

     /** @param node_id the id of the source node.
     * @return a collection of edges that start from the node.
      * @return an empty list if the node is not valid.
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if (nodes.containsKey(node_id)) {   
            NodeData node = (NodeData) nodes.get(node_id);
            return node.getE();  
        }
        LinkedList<edge_data> emptyList = new LinkedList<>();
        return emptyList;
    }

    /**
     * @param node_id the id of the destination node
     * @return a collection of all the edges that end in this node.
     * @return an empty list if the node is not valid.
     */
    public Collection<edge_data> getBE(int node_id) {
    	 LinkedList<edge_data> emptyList = new LinkedList<>();
        if (nodes.containsKey(node_id)) {  
            NodeData node = (NodeData) nodes.get(node_id);
            return node.getBE(); 
        }
       
        return emptyList;
    }

    /** remove the node from the graph and the edges
     * he is connected to. if the node is not valid
     * nothing changes.
     * @param key the key of the requested node.
     * @return the removed node. null if the node is not valid.
     */
    @Override
    public node_data removeNode(int key) {
        if (nodes.containsKey(key)) {       //check if the node exist
            NodeData node = (NodeData) nodes.get(key);
            Collection<edge_data> edges = node.getE();
            edge_data edgesArray[] = edges.toArray(new edge_data[0]); 
            for(edge_data e : edgesArray){
                this.removeEdge(e.getSrc(),e.getDest());
            }
            edges = node.getBE();
            edgesArray = edges.toArray(new edge_data[0]);   
            for(edge_data e : edgesArray){
                this.removeEdge(e.getSrc(),e.getDest());
            }
            nodes.remove(key);
            vertices.remove(node);
            mC++;
            return node;
        }
        return null;
    }

   
    @Override
    public edge_data removeEdge(int src, int dest) {
        if (nodes.containsKey(src) && nodes.containsKey(dest)) { //check if the nodes are valid
            NodeData srcNode = (NodeData) nodes.get(src);
            NodeData destNode = (NodeData) nodes.get(dest);
            destNode.removeBackEdge(src);              
            edge_data edge = srcNode.removeEdge(dest);  
            if(edge != null) {
                edgeCounter--;                     
                mC++;
            }
            return edge;
        }
        return null;
    }

    /** @return the number of nodes in the graph. */
    @Override
    public int nodeSize() {
        return vertices.size();
    }

    /** @return the number of edges in the graph. */
    @Override
    public int edgeSize() {
        return edgeCounter;
    }

    /** @return the number of modifications done to the graph. */
    @Override
    public int getMC() {
        return mC;
    }

    /** a toString function for the graph.
     * prints the number of nodes edges in the graph,
     * along with a list of every node and his neighbors edges and tags.
     * @return
     */
    public String toString(){
        String s = "nodes: "+nodes.size()+", edges: "+edgeCounter;    
        Collection<node_data> graphList = this.getV();
        for(node_data node : graphList){                                
            NodeData nodeI = (NodeData) node;                            
            s += "\n" + nodeI.toString();                           //add its information to the main info String
        }
        return s;
    }

    ///////////////////////EdgeData_class////////////////////////////////

   
    private class EdgeData implements edge_data {

        private node_data _src, _dest;  
        private double _weight;         
        private int _tag = 0;          
        private String _info;         

      
        public EdgeData(double weight, node_data src, node_data dest) {
            _weight = weight;
            _src = src;
            _dest = dest;
        }

       
        @Override
        public int getSrc() {
            return _src.getKey();
        }

        //return the id of the dest node
        @Override
        public int getDest() {
            return _dest.getKey();
        }

        //return the weight of the edge
        @Override
        public double getWeight() {
            return _weight;
        }

        //return the info of the edge
        @Override
        public String getInfo() {
            return _info;
        }

        //set the info of the edge
        @Override
        public void setInfo(String s) {
            _info = s;
        }

        //return the tag of the edge
        @Override
        public int getTag() {
            return _tag;
        }

        //set the edge's tag
        @Override
        public void setTag(int t) {
            _tag = t;
        }
    }
}


