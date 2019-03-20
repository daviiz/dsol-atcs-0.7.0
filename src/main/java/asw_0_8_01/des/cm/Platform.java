package asw_0_8_01.des.cm;

import devs.core.CoupledModelBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class Platform extends CoupledModelBase {
    public Platform(String modelName) {
        super(modelName);
    }

    public Platform(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Platform(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {

    }

    @Override
    protected void couplingComponent() {

    }
}
