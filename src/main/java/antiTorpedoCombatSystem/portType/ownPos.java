package antiTorpedoCombatSystem.portType;

import devs.core.PortTypeBase;
import lombok.Data;
import nl.tudelft.simulation.language.d3.CartesianPoint;
@Data
public class ownPos extends PortTypeBase {
    private CartesianPoint position= new CartesianPoint(Double.NaN,Double.NaN,Double.NaN);
    private String name;
    private int cmp;
    private boolean isLive;
    private double detectRange;
}
