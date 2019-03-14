package combatSysModel.portType;

import devs.core.PortTypeBase;
import nl.tudelft.simulation.language.d3.CartesianPoint;

public class env_info  extends PortTypeBase {
    public CartesianPoint location = new CartesianPoint(0, 0, 0);
    public int camp = 0;
    public String entityName = "";
    public boolean isLive = true;
}
