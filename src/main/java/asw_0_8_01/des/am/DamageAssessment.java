package asw_0_8_01.des.am;

import asw_0_8_01.om.DamageAssessmentOm;
import asw_0_8_01.portMsgType.engage_result;
import asw_0_8_01.portMsgType.move_result;
import asw_0_8_01.portMsgType.scen_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class DamageAssessment extends AtomicModelBase<DamageAssessmentOm> {

    public InputPortBase<scen_info> in_scen_info;
    public InputPortBase<move_result> in_move_result;

    public OutputPortBase<engage_result> out_engage_result;

    public DamageAssessment(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public DamageAssessment(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_scen_info = new InputPortBase<scen_info>(this);
        in_move_result = new InputPortBase<move_result>(this);

        out_engage_result = new OutputPortBase<engage_result>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new DamageAssessmentOm();
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
