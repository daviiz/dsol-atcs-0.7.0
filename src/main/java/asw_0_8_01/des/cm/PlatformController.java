package asw_0_8_01.des.cm;

import asw_0_8_01.des.am.PlatformControllerActor;
import asw_0_8_01.des.am.PlatformControllerUpdater;
import asw_0_8_01.portMsgType.*;
import devs.core.CoupledModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class PlatformController extends CoupledModelBase {

    public InputPortBase<Boolean> in_engage_result;
    public InputPortBase<scen_info> in_scen_info;
    public InputPortBase<threat_info> in_threat_info;
    public InputPortBase<Boolean> in_move_finished;
    public InputPortBase<guidance_info> in_guidance_info;

    public OutputPortBase<String> out_wp_launch;
    public OutputPortBase<String> out_move_cmd;
    public OutputPortBase<wp_guidance> out_wp_guidance;

    public PlatformControllerActor actor;
    public PlatformControllerUpdater updater;

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
        in_engage_result = new InputPortBase<Boolean>(this);
        in_scen_info = new InputPortBase<scen_info>(this);
        in_threat_info = new InputPortBase<threat_info>(this);
        in_move_finished = new InputPortBase<Boolean>(this);
        in_guidance_info = new InputPortBase<guidance_info>(this);

        out_wp_launch = new OutputPortBase<String>(this);
        out_move_cmd = new OutputPortBase<String>(this);
        out_wp_guidance = new OutputPortBase<wp_guidance>(this);
    }

    @Override
    protected void couplingComponent() {
        actor = new PlatformControllerActor("actor",this);
        actor.constructModel();
        updater = new PlatformControllerUpdater("updater",this);
        updater.constructModel();

        /**
         * EIC
         */
        this.addExternalInputCoupling(this.in_move_finished,actor.in_move_finished);
        this.addExternalInputCoupling(this.in_engage_result,actor.in_engage_result);
        this.addExternalInputCoupling(this.in_scen_info,actor.in_scen_info);
        this.addExternalInputCoupling(this.in_scen_info,updater.in_scen_info);
        this.addExternalInputCoupling(this.in_threat_info,updater.in_threat_info);
        this.addExternalInputCoupling(this.in_guidance_info,actor.in_guidance_info);

        /**
         * EOC
         */
        this.addExternalOutputCoupling(actor.out_move_cmd,this.out_move_cmd);
        this.addExternalOutputCoupling(actor.out_wp_launch,this.out_wp_launch);
        this.addExternalOutputCoupling(actor.out_wp_guidance,this.out_wp_guidance);

        /**
         * IC
         */
        this.addInternalCoupling(updater.out_target_info,actor.in_target_info);

    }
}
