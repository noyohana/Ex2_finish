package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/** a node class that will be hold in the DWGraph_DS class.
 */
public class NodeData implements node_data{

    private int key;               
    private double weight = 0;     
    private int tag = 0;           
    private String _info;          
    private geo_location position = new GeoLocation(0,0,0);    
    private HashMap<Integer, edge_data> MapedgesDest = new HashMap<>(); 
    private HashMap<Integer, edge_data> MapedgesSrc = new HashMap<>();  
    private LinkedList<edge_data> listEdgeSrc = new LinkedList<>();    
    private LinkedList<edge_data> listEdgeDest = new LinkedList<>();   

    /** constructor of the node sets the node's id.
     *  the id cant be changed after.
     * @param key
     */
    public NodeData(int k){
        key = k;
    }

    /** @return the node's key */
    @Override
    public int getKey() {
        return key;
    }

    /**
     * @return return the position of the node.
     */
    @Override
    public geo_location getLocation() {
        return position;
    }

    /** set the node's location.
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        position = p;
    }

    /** add an edge that start from this node.
     *  does nothing if the edge already exist.
     * @param e the edge that connects this node.
     */
    public void addEdge(edge_data e){
        int dest = e.getDest();
        if(!MapedgesDest.containsKey(dest)){  //check if the edge exist
            MapedgesDest.put(dest, e);        //add the edge
            listEdgeSrc.add(e);
        }
    }

    /** add an edge that ends in this node.
     *  does nothing if the edge already exist.
     * @param e the edge pointing to the node.
     */
    public void backEdge(edge_data e){
        int src = e.getSrc();
        if(!MapedgesSrc.containsKey(src)){  
            MapedgesSrc.put(src,e);       
            listEdgeDest.add(e);
        }
    }

    /** remove the edge pointing to this node.
     *  if the edge does not exist, does nothing.
     * @param src the source node of the edge.
     */
    public void removeBackEdge(int src){
        if(MapedgesSrc.containsKey(src)){          
            edge_data edge = MapedgesSrc.get(src); 
            MapedgesSrc.remove(src);
            listEdgeDest.remove(edge);
        }
    }

    /** remove the edge that start in this node.
     *  if the the node does not exist, does nothing
     * @param dest the destination node of the edge.
     * @return the remove edge. null if none.
     */
    public edge_data removeEdge(int dest){
        if(MapedgesDest.containsKey(dest)){           
            edge_data edge = MapedgesDest.get(dest);    
            MapedgesDest.remove(dest);
            listEdgeSrc.remove(edge);
            return edge;
        }
        return null;
    }

    /** does nothing if the edge does not exist.
     * @param dest the destination node of the edge.
     * @return the requested edge that ends in dest.
     * @return null if none.
     */
    public edge_data getEdge(int dest){
        if(MapedgesDest.containsKey(dest)){  //if exist return edge
            return MapedgesDest.get(dest);
        }
        return null;
    }

    /** @return if the edge exist. */
    public boolean hasEdge(int dest){
        return MapedgesDest.containsKey(dest);
    }

    /** @return a collection of edges that start from this node. */
    public Collection<edge_data> getE(){
        return listEdgeSrc;
    }

    /** @return a collection of nodes that end in this node. */
    public Collection<edge_data> getBE(){
        return listEdgeDest;
    }

    /** @return theweight of the node */
    @Override
    public double getWeight() {
        return weight;
    }

    /** set the node's weight.
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        weight = w;
    }

    /** @return the info of the node. */
    @Override
    public String getInfo() {
        return _info;
    }

    /** sets the node's info.
     * @param s wanted string info.
     */
    @Override
    public void setInfo(String s) {
        _info = s;
    }

    /** @return the tag of the node. */
    @Override
    public int getTag() {
        return tag;
    }

    /** sets the node's tag.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        tag = t;
    }
    /** a toString function.
     * prints the node's key and all his neighbors and edges
     * @return a string that hold all the information of the node
     */
    public String toString(){
        String info = "[" + key + "]:";                    
        for(edge_data e : listEdgeSrc){                              
            info += " ["+e.getDest()+","+e.getWeight()+"]";  
        }
        info +=". tag = " + tag + ", ";                    
        info +="weight = " + weight + ". ";                 
        info += position;                                        
        return info;
    }
}
