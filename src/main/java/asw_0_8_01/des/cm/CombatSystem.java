package asw_0_8_01.des.cm;

import devs.core.CMRootBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class CombatSystem extends CMRootBase {
    public CombatSystem(String modelName) {
        super(modelName);
    }

    public CombatSystem(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public CombatSystem(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructComponent() {

    }

    @Override
    protected void couplingComponent() {

    }
}
