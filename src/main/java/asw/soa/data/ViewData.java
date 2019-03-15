package asw.soa.data;

import combatSysModel.portType.scen_info;
import nl.tudelft.simulation.language.d3.CartesianPoint;

import java.awt.*;

/**
 * @author daiwenzhi
 */
public class ViewData implements java.io.Serializable {

    /**
     * the radius of the ball.
     */
    public static final double RADIUS = 5.0;
    /**
     *
     */
    private static final long serialVersionUID = -3177965369711352742L;
    /**
     * the origin.
     */
    public CartesianPoint origin = new CartesianPoint(0, 0, 0);
    /**
     * the destination.
     */
    public CartesianPoint destination = new CartesianPoint(0, 0, 0);
    /**
     * the start time.
     */
    public double startTime = Double.NaN;
    /**
     * the stop time.
     */
    public double stopTime = Double.NaN;
    /**
     * the angle of the ball.
     */
    public double theta = 0.0;

    public boolean isActive = true;

    /**
     * the name of the model.
     */
    public String name = "default";

    public int belong = 1;

    public boolean status = false;

    public double speed = 0;

    public int detectRange = 100;

    public LineData lineData = new LineData(0, 0, 0, 0);

    /**
     * 通信数据链
     */
    public int x1 = 0;
    public int y1 = 0;
    public int x2 = 0;
    public int y2 = 0;

    public Color color = Color.RED;


    public ViewData(Color color, int detectRange, int x1, int y1, int x2, int y2) {
        this.color = color;
        this.detectRange = detectRange;
        this.x1 = x1;
        this.y1 = y1;

        this.x2 = x2;
        this.y2 = y2;
    }

    public ViewData() {

    }

    public ViewData(String name) {
        this.name = name;
        if (this.name.contains("Fleet")) {
            this.color = Color.RED;
            this.detectRange = 200;
            this.belong = 1;
            this.speed = 2;
        } else if (this.name.contains("Submarine")) {
            this.color = Color.BLUE;
            this.detectRange = 400;
            this.belong = -1;
            this.speed = 1;

        } else if (this.name.contains("Decoy")) {
            this.color = Color.PINK;
            this.detectRange = 100;
            this.belong = 1;
            this.speed = 2;

        } else if (this.name.contains("Torpedo")) {
            this.color = Color.CYAN;
            this.detectRange = 150;
            this.belong = -1;
            this.speed = 4;
        }
    }
    public ViewData(scen_info info){
        this.color = info.color;
        this.detectRange = info.detectRange;
        this.x1 = info.x1;
        this.y1 = info.y1;

        this.x2 = info.x2;
        this.y2 = info.y2;

        this.origin = info.origin;
        this.destination = info.destination;

        this.startTime = info.startTime;
        this.stopTime = info.stopTime;
        this.belong = info.camp;
    }

    public String toString() {
        return this.name;
    }
}
