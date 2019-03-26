package asw_0_8_01.des.cm;

import asw_0_8_01.des.am.PlatformManeuverActor;
import asw_0_8_01.des.am.PlatformManeuverUpdater;
import asw_0_8_01.portMsgType.env_info;
import asw_0_8_01.portMsgType.move_result;
import asw_0_8_01.portMsgType.scen_info;
import devs.core.CoupledModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class PlatformManeuver extends CoupledModelBase {

    public InputPortBase<Boolean> in_engage_result;
    public InputPortBase<scen_info> in_scen_info;
    public InputPortBase<env_info> in_env_info;
    public InputPortBase<String> in_move_cmd;

    public OutputPortBase<Boolean> out_move_finished;
    public OutputPortBase<Boolean> out_fuel_exhasuted;
    public OutputPortBase<move_result> out_move_result;

    public  PlatformManeuverActor actor;
    public  PlatformManeuverUpdater updater;

    public PlatformManeuver(String modelName) {
        super(modelName);
    }

    public PlatformManeuver(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformManeuver(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_engage_result = new InputPortBase<Boolean>(this);
        in_scen_info = new InputPortBase<scen_info>(this);
        in_env_info = new InputPortBase<env_info>(this);
        in_move_cmd = new InputPortBase<String>(this);

        out_move_finished = new OutputPortBase<Boolean>(this);
        out_fuel_exhasuted = new OutputPortBase<Boolean>(this);
        out_move_result = new OutputPortBase<move_result>(this);

    }

    @Override
    protected void couplingComponent() {
        actor = new PlatformManeuverActor("actor",this);
        updater = new PlatformManeuverUpdater("updater",this);
        actor.constructModel();
        updater.constructModel();

        /**
         * EIC
         */
        this.addExternalInputCoupling(this.in_engage_result,actor.in_engage_result);
        this.addExternalInputCoupling(this.in_env_info,actor.in_env_info);
        this.addExternalInputCoupling(this.in_scen_info,actor.in_scen_info);
        this.addExternalInputCoupling(this.in_move_cmd,updater.in_move_cmd);

        /**
         * EOC
         */
        this.addExternalOutputCoupling(actor.out_fuel_exhasuted,this.out_fuel_exhasuted);
        this.addExternalOutputCoupling(actor.out_move_finished,this.out_move_finished);
        this.addExternalOutputCoupling(actor.out_move_result,this.out_move_result);

        /**
         * IC
         */
        this.addInternalCoupling(updater.out_cmd_info,actor.in_cmd_info);
    }
}
