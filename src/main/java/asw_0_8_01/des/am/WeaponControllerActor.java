package asw_0_8_01.des.am;

import asw_0_8_01.om.WeaponControllerActorOm;
import asw_0_8_01.portMsgType.entity_info;
import combatSysModel.portType.target_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class WeaponControllerActor extends AtomicModelBase<WeaponControllerActorOm> {

    public InputPortBase<Boolean> in_move_finished;
    public InputPortBase<Boolean> in_engage_result;
    public InputPortBase<entity_info[]> in_entity_info;
    public InputPortBase<target_info> in_target_info;

    public OutputPortBase<String> out_move_cmd;

    public WeaponControllerActor(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public WeaponControllerActor(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_move_finished = new InputPortBase<Boolean>(this);
        in_engage_result = new InputPortBase<Boolean>(this);
        in_entity_info = new InputPortBase<entity_info[]>(this);
        in_target_info = new InputPortBase<target_info>(this);

        out_move_cmd = new OutputPortBase<String>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new WeaponControllerActorOm();
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
