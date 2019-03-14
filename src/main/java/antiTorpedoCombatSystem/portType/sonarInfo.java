package antiTorpedoCombatSystem.portType;

import devs.core.PortTypeBase;
import lombok.Data;
import nl.tudelft.simulation.language.d3.CartesianPoint;
@Data
public class sonarInfo extends PortTypeBase {
    private CartesianPoint position;
    private String name;
    private int cmp;
    private boolean isLive;
    private double detectRange;
    private boolean isSelf = false;

}
