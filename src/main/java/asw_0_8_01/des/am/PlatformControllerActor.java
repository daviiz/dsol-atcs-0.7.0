package asw_0_8_01.des.am;

import asw_0_8_01.om.PlatformControllerActorOm;
import asw_0_8_01.portMsgType.*;
import combatSysModel.portType.target_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class PlatformControllerActor extends AtomicModelBase<PlatformControllerActorOm> {

    public InputPortBase<Boolean> in_move_finished;
    public InputPortBase<Boolean> in_engage_result;
    public InputPortBase<scen_info> in_scen_info;
    public InputPortBase<target_info> in_target_info;
    public InputPortBase<guidance_info> in_guidance_info;

    public OutputPortBase<wp_launch> out_wp_launch;
    public OutputPortBase<String> out_move_cmd;
    public OutputPortBase<wp_guidance> out_wp_guidance;

    public PlatformControllerActor(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformControllerActor(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_engage_result = new InputPortBase<Boolean>(this);
        in_scen_info = new InputPortBase<scen_info>(this);
        in_target_info = new InputPortBase<target_info>(this);
        in_move_finished = new InputPortBase<Boolean>(this);
        in_guidance_info = new InputPortBase<guidance_info>(this);

        out_wp_launch = new OutputPortBase<wp_launch>(this);
        out_move_cmd = new OutputPortBase<String>(this);
        out_wp_guidance = new OutputPortBase<wp_guidance>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new PlatformControllerActorOm();
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
