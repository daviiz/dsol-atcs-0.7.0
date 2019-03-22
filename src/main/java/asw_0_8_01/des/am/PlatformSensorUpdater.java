package asw_0_8_01.des.am;

import asw_0_8_01.om.PlatformSensorUpdaterOm;
import asw_0_8_01.portMsgType.move_result;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class PlatformSensorUpdater extends AtomicModelBase<PlatformSensorUpdaterOm> {

    public InputPortBase<move_result> in_move_result;

    public InputPortBase<String> in_request;
    public OutputPortBase<String> out_response;

    public PlatformSensorUpdater(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformSensorUpdater(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_move_result = new InputPortBase<move_result>(this);
        in_request = new InputPortBase<String>(this);
        out_response = new OutputPortBase<String>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new PlatformSensorUpdaterOm();
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
