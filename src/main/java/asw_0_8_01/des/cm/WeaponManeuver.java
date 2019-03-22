package asw_0_8_01.des.cm;

import asw_0_8_01.des.am.WeaponManeuverActor;
import asw_0_8_01.des.am.WeaponManeuverUpdater;
import asw_0_8_01.portMsgType.*;
import devs.core.CoupledModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class WeaponManeuver extends CoupledModelBase {

    public InputPortBase<Boolean> in_engage_result;
    public InputPortBase<env_info> in_env_info;
    public InputPortBase<entity_info> in_entity_info;
    public InputPortBase<String> in_move_cmd;

    public OutputPortBase<Boolean> out_move_finished;
    public OutputPortBase<Boolean> out_fuel_exhasuted;
    public OutputPortBase<move_result> out_move_result;

    private WeaponManeuverActor actor;
    private WeaponManeuverUpdater updater;

    public WeaponManeuver(String modelName) {
        super(modelName);
    }

    public WeaponManeuver(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public WeaponManeuver(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_engage_result = new InputPortBase<Boolean>(this);
        in_env_info = new InputPortBase<env_info>(this);
        in_entity_info = new InputPortBase<entity_info>(this);
        in_move_cmd = new InputPortBase<String>(this);

        out_move_finished = new OutputPortBase<Boolean>(this);
        out_fuel_exhasuted = new OutputPortBase<Boolean>(this);
        out_move_result = new OutputPortBase<move_result>(this);
    }

    @Override
    protected void couplingComponent() {
        actor = new WeaponManeuverActor("actor",this);
        updater = new WeaponManeuverUpdater("updater",this);
        actor.constructModel();
        updater.constructModel();

        /**
         * EIC
         */
        this.addExternalInputCoupling(this.in_engage_result,actor.in_engage_result);
        this.addExternalInputCoupling(this.in_env_info,actor.in_env_info);
        this.addExternalInputCoupling(this.in_entity_info,actor.in_entity_info);
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
