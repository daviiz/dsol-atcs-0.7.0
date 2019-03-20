package asw_0_8_01.des.cm;

import devs.core.CoupledModelBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class PlatformSensor extends CoupledModelBase {
    public PlatformSensor(String modelName) {
        super(modelName);
    }

    public PlatformSensor(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformSensor(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {

    }

    @Override
    protected void couplingComponent() {

    }
}
