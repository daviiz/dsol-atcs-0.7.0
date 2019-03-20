package asw_0_8_01.des.cm;

import devs.core.CoupledModelBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class PlatformManeuver extends CoupledModelBase {
    public PlatformManeuver(String modelName) {
        super(modelName);
    }

    public PlatformManeuver(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformManeuver(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {

    }

    @Override
    protected void couplingComponent() {

    }
}