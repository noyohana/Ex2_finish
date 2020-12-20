package gameClient;

import api.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Player {

    private static Arena _ar;
    private CL_Agent _agent;
    private CL_Pokemon _pokemon;
    private static HashSet<String> occuped;
    private static HashMap<Integer,HashSet<String>> permitted;
    private static directed_weighted_graph _graph;
    private static DWGraphs_Algo _graphAlgo;
    private static game_service _game;
    private int _reset = 0;


    public Player(Arena ar, directed_weighted_graph graph, game_service game, int numOfAgents){
        _ar = ar;
        _graph = graph;
        _game = game;
        occuped = new HashSet<>();
        permitted= new HashMap<>();
        _graphAlgo = new DWGraphs_Algo(new DWGraph_DS());
        DWGraph_DS temp = _graphAlgo.caster(_graph);
        _graphAlgo.init(temp);
        for(int i = 0; i < numOfAgents; i++){
        	permitted.put(i,new HashSet<String>());
        }
    }

    public synchronized int init(CL_Agent agent){
        _agent = agent;
        int nextNode = moveAgents();
        if(_reset == 100* permitted.size()){
        	   occuped.clear();
               for(int i = 0; i < permitted.size(); i++){
               	permitted.get(i).clear();
               }
               _reset = 0;
        }
        double slpTime = 100;
        try{
            slpTime = getTime(nextNode);
        }
        catch (NullPointerException ne){
            try{
                wait();
                slpTime = 0;
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return (int) slpTime;
    }

    private long getTime(int nextNode){
        double slpTime;
        double speed = _agent.getSpeed();
        edge_data Edge = _pokemon.get_edge();
        int currentNode = _agent.getSrcNode();
        edge_data currentEdge = _graph.getEdge(currentNode,nextNode);
        geo_location srcPosition = _graph.getNode(currentNode).getLocation();
        geo_location destPosition = _graph.getNode(nextNode).getLocation();
        double Distance = srcPosition.distance(destPosition)*1000;
        double weight = currentEdge.getWeight();
        if((Edge.getDest() == currentEdge.getDest()) && (Edge.getSrc() == currentEdge.getSrc())){
            double distance2 = _agent.getLocation().distance(_pokemon.getLocation())*1000;
            double ans = distance2/Distance;
            weight *= ans;
        }
        else{
            double dist2Dest = _agent.getLocation().distance(destPosition)*1000;
            double ratio = (dist2Dest/Distance);
            weight *= ratio;
        }
        weight *= 1000;
        slpTime = weight/speed;
        slpTime++;
        return (long) slpTime;
    }

    private int moveAgents() {
        String s = _game.move();
        List<CL_Agent> log = Arena.getAgents(s, _graph);
        _ar.setAgents(log);
        String fs = _game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);
        notify();
        _agent = log.get(_agent.getID());
        int id = _agent.getID();
        int src = _agent.getSrcNode();
        double v = _agent.getValue();
        int dest = this.nextNode(src);
        _game.chooseNextEdge(_agent.getID(), dest);
        System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
        return dest;
    }

    private int nextNode(int src) {
        int ans = -1;
            List<CL_Pokemon> pokes = _ar.getPokemons();
            int Dest = findPkm(pokes, src);
            List<node_data> Listpath = _graphAlgo.shortestPath(src, Dest);
            for(int i = 0; i < permitted.size()-1; i++){
                NextPokemon(pokes, Dest);
            }
            if(Listpath != null){
                Listpath.remove(0);
                ans = Listpath.get(0).getKey();
            }
        return ans;
    }

    private int findPkm(List<CL_Pokemon> pokes, int src) {
        int ans = -1;
        double Distance = Integer.MAX_VALUE;
        CL_Pokemon pokemon = null;
        for (CL_Pokemon pok : pokes) {
            String position = pok.getLocation().toString();
            if(!occuped.contains(position) || permitted.get(_agent.getID()).contains(position)){
                edge_data edge = pok.get_edge();
                int edgedest = edge.getDest();
                if (edgedest == src) {
                    return edge.getSrc();
                }
                double distance = _graphAlgo.shortestPathDist(src,edgedest);
                if (distance < Distance && distance != -1) {
                    Distance = distance;
                    ans = edge.getDest();;
                    _pokemon = pok;
                }
            }
        }
        if(pokemon != null){
            occuped.add(_pokemon.getLocation().toString());
            permitted.get(_agent.getID()).add(_pokemon.getLocation().toString());
            _reset++;
        }
        return ans;
    }

    private void NextPokemon(List<CL_Pokemon> pokes, int src) {
        double minDist = Integer.MAX_VALUE;
        CL_Pokemon pokemon = null;
        for (CL_Pokemon pok : pokes) {
            String pos = pok.getLocation().toString();
            if(!occuped.contains(pos) ||permitted.get(_agent.getID()).contains(pos)){
                edge_data edge = pok.get_edge();
                int edgeDest = edge.getDest();
                double dist = _graphAlgo.shortestPathDist(src,edgeDest);
                if (dist < minDist && dist != -1) {
                    minDist = dist;
                    pokemon = pok;
                }
            }
        }
        if(pokemon != null){
            occuped.add(pokemon.getLocation().toString());
            permitted.get(_agent.getID()).add(pokemon.getLocation().toString());
            _reset++;
        }
    }


}
