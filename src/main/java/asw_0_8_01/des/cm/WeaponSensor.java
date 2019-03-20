package asw_0_8_01.des.cm;

import devs.core.CoupledModelBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class WeaponSensor  extends CoupledModelBase {
    public WeaponSensor(String modelName) {
        super(modelName);
    }

    public WeaponSensor(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public WeaponSensor(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {

    }

    @Override
    protected void couplingComponent() {

    }
}
