package asw_0_8_01.des.cm;

import devs.core.CoupledModelBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class WeaponManeuver extends CoupledModelBase {
    public WeaponManeuver(String modelName) {
        super(modelName);
    }

    public WeaponManeuver(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public WeaponManeuver(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {

    }

    @Override
    protected void couplingComponent() {

    }
}
