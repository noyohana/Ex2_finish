package api;


public class GeoLocation implements geo_location {

    private double _x, _y, _z;   
    
    /** constructor to set the position of the object. */
    public GeoLocation(double x, double y, double z){
        _x = x;
        _y = y;
        _z = z;
    }

    /** @return the X value of the position. */
    @Override
    public double x() {
        return _x;
    }

    /** @return the Y value of the position. */
    @Override
    public double y() {
        return _y;
    }

    /** @return the Z value of the position. */
    @Override
    public double z() {
        return _z;
    }

  
    @Override
    public double distance(geo_location g) { 
        double dist = Math.sqrt(Math.pow((g.x() - _x),2) + Math.pow((g.y() - _y), 2) + Math.pow((g.z() - _z), 2));
        return dist;
    }

    /** @return a string of the position of the object. */
    public String toString(){
        String info = _x + "," + _y + "," + _z;
        return info;
    }
}
