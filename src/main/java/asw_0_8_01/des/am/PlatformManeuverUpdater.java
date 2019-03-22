package asw_0_8_01.des.am;

import asw_0_8_01.om.PlatformManeuverUpdaterOm;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class PlatformManeuverUpdater extends AtomicModelBase<PlatformManeuverUpdaterOm> {

    public InputPortBase<String> in_move_cmd;

    public OutputPortBase<String> out_cmd_info;

    public PlatformManeuverUpdater(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformManeuverUpdater(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_move_cmd = new InputPortBase<String>(this);

        out_cmd_info = new OutputPortBase<String>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new PlatformManeuverUpdaterOm();
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
