package combatSysModel.portType;

import devs.core.PortTypeBase;
import nl.tudelft.simulation.language.d3.CartesianPoint;

public class move_result extends PortTypeBase {
    public CartesianPoint location = new CartesianPoint(0, 0, 0);
    public int camp = 0;
}
