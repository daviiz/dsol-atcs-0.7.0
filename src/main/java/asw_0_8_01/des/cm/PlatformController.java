package asw_0_8_01.des.cm;

import devs.core.CoupledModelBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class PlatformController extends CoupledModelBase {



    public PlatformController(String modelName) {
        super(modelName);
    }

    public PlatformController(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformController(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {

    }

    @Override
    protected void couplingComponent() {

    }
}
