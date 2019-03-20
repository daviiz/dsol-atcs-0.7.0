package asw_0_8_01.des.cm;

import devs.core.CoupledModelBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class WeaponController extends CoupledModelBase {
    public WeaponController(String modelName) {
        super(modelName);
    }

    public WeaponController(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public WeaponController(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {

    }

    @Override
    protected void couplingComponent() {

    }
}
