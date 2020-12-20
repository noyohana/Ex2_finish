package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph. an improved MyFrame class.
 * shows the graph, agents, pokemons, score and a timer
 * to the end of the game.
 */
public class Frame extends JFrame{
    private Arena _ar;                          
    private gameClient.util.Range2Range _w2f;   
    private game_service _game;                 //game service
    private Image graph;                    
    private int _scenario;    
    private Image pokemon1;
    private Image pokemon2;

    /** a constructor for initializing the frame variables.
     * @param a the name of the frame.
     * @param game the game service object.
     * @param level the current level.
     */
    Frame(String a, game_service game) {
        super(a);
        _game = game;
        this.pokemon1 = new ImageIcon("./data/pika9.png").getImage();
        this.pokemon2 = new ImageIcon("./data/pika3.png").getImage();
    }

    /** updates the frame..
     *  resizing the graph to the current size of the frame window.
     * @param ar the current game arena
     */
    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();
    }


     //using the w2f to resize the graph. updates the range of the graph.
    private void updateFrame() {
        Range rx = new Range(20,this.getWidth()-20);     //calculate the new range for the graph.
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g,frame);
    }

    /** paints the game on the frame.
     *  draws, redraws all the game elements in the current states.
     * @param g the main graphics object.
     */
    public void paint(Graphics g) {
        int w = this.getWidth();                     //scale the image to window size
        int h = this.getHeight();
        graph = this.createImage(w,h);
        Graphics graphics = graph.getGraphics(); //create an image of the graph that we will paint on
        paintComponents(graphics);
        g.drawImage(graph,0,0,this);
        updateFrame();                              //update the frame image in case of a resize
    }

    /** paints all the game components on the games GUI
     * @param g current graphics object.
     */
    @Override
    public void paintComponents(Graphics g){
        drawPokemons(g);
        drawGraph(g);
        drawAgents(g);
        drawTimer(g);
      
    }

  
    // draws the game's timer to the end of the game.
    private void drawTimer(Graphics g){
        g.setFont(new Font("Arial",Font.BOLD,36));
        int sec = (int) (_game.timeToEnd()/1000);   //calculate the amount of seconds
        int min = (int) (_game.timeToEnd()/60000);  //calculate the amount of minutes
        String time = min+":"+sec;                  //timer format
        g.drawString(time,20,70);
    }



   
    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while(iter.hasNext()) {           
            node_data n = iter.next();
            g.setColor(Color.blue);
            drawNode(n,5,g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while(itr.hasNext()) {         
                edge_data e = itr.next();
                g.setColor(Color.gray);
                drawEdge(e, g);
            }
        }
    }

  
    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> fs = _ar.getPokemons();
        if(fs!=null) {                                     
            Iterator<CL_Pokemon> itr = fs.iterator();
            while(itr.hasNext()) {                          //draw each pokemon on his location
                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r=10;
                g.setColor(Color.green);
                geo_location fp = this._w2f.world2frame(c);
                if(f.getType()<0) { g.drawImage(pokemon1, (int) fp.x() - r, (int) fp.y() - r, 4 * r, 4 * r, null);}
                if(c!=null) {
                  
                	g.drawImage(pokemon2, (int) fp.x() - r, (int) fp.y() - r, 4 * r, 4 * r, null);
                   
                }
            }
        }
    }

  
    private void drawAgents(Graphics g) {
        List<CL_Agent> rs = _ar.getAgents();
        g.setColor(Color.red);
        int i=0;
        while(rs!=null && i<rs.size()) {               
            geo_location c = rs.get(i).getLocation();
            int r=8;
            i++;
            if(c!=null) {      
                geo_location fp = this._w2f.world2frame(c);
                g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
            }
        }
    }

   
    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);        
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
    }

  
    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);                  
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
    }
}
