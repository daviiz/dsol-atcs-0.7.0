package asw_0_8_01.des.am;

import asw_0_8_01.portMsgType.env_info;
import asw_0_8_01.portMsgType.move_result;
import asw_0_8_01.portMsgType.scen_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.ObjectModelBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class CommunicateBus extends AtomicModelBase<ObjectModelBase> {

    public InputPortBase<move_result> in_move_result;
    public OutputPortBase<move_result> out_move_result;

    @Override
    protected void constructPort() {
        in_move_result = new InputPortBase<move_result>(this);
        out_move_result = new OutputPortBase<move_result>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new ObjectModelBase();
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

    public CommunicateBus(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public CommunicateBus(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }
}
