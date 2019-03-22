package asw_0_8_01.des.am;

import asw_0_8_01.om.WeaponManeuverUpdaterOm;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class WeaponManeuverUpdater extends AtomicModelBase<WeaponManeuverUpdaterOm> {

    public InputPortBase<String> in_move_cmd;

    public OutputPortBase<String> out_cmd_info;

    public WeaponManeuverUpdater(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    public WeaponManeuverUpdater(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    @Override
    protected void constructPort() {
        in_move_cmd = new InputPortBase<String>(this);

        out_cmd_info = new OutputPortBase<String>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new WeaponManeuverUpdaterOm();
    }

    @Override
    protected void constructPhase() {

    }

    @Override
    protected void deltaExternalFunc(Object value) {

    }

    @Override
    protected void deltaInternalFunc() {

    }

    @Override
    protected void lambdaFunc() {

    }
}
