package asw_0_8_01.des.cm;

import devs.core.CoupledModelBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class Weapon extends CoupledModelBase {
    public Weapon(String modelName) {
        super(modelName);
    }

    public Weapon(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Weapon(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {

    }

    @Override
    protected void couplingComponent() {

    }
}
