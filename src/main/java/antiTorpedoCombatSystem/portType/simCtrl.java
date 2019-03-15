package antiTorpedoCombatSystem.portType;

import devs.core.PortTypeBase;
import lombok.Data;
import nl.tudelft.simulation.language.d3.CartesianPoint;

@Data
public class simCtrl extends PortTypeBase {

    private String START = "START",PAUSE = "PAUSE",STOP = "STOP";

    private String COMMAND = "FIRE";
    private CartesianPoint orginPos= new CartesianPoint(Double.NaN,Double.NaN,Double.NaN);
    private CartesianPoint targetPos= new CartesianPoint(Double.NaN,Double.NaN,Double.NaN);
}
