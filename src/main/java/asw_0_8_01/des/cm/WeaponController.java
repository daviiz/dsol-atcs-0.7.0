package asw_0_8_01.des.cm;

import asw_0_8_01.des.am.WeaponControllerActor;
import asw_0_8_01.des.am.WeaponControllerUpdater;
import asw_0_8_01.portMsgType.*;
import devs.core.CoupledModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class WeaponController extends CoupledModelBase {

    public InputPortBase<Boolean> in_move_finished;
    public InputPortBase<Boolean> in_engage_result;
    public InputPortBase<entity_info> in_entity_info;
    public InputPortBase<threat_info> in_threat_info;

    public OutputPortBase<String> out_move_cmd;

    private WeaponControllerActor actor;
    private WeaponControllerUpdater updater;

    public WeaponController(String modelName) {
        super(modelName);
    }

    public WeaponController(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public WeaponController(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_move_finished = new InputPortBase<Boolean>(this);
        in_engage_result = new InputPortBase<Boolean>(this);
        in_entity_info = new InputPortBase<entity_info>(this);
        in_threat_info = new InputPortBase<threat_info>(this);

        out_move_cmd = new OutputPortBase<String>(this);
    }

    @Override
    protected void couplingComponent() {
        actor = new WeaponControllerActor("actor",this);
        updater = new WeaponControllerUpdater("updater",this);
        actor.constructModel();
        updater.constructModel();

        /**
         * EIC
         */
        this.addExternalInputCoupling(this.in_move_finished,actor.in_move_finished);
        this.addExternalInputCoupling(this.in_engage_result,actor.in_engage_result);
        this.addExternalInputCoupling(this.in_entity_info,actor.in_entity_info);
        this.addExternalInputCoupling(this.in_threat_info,updater.in_threat_info);

        /**
         * EOC
         */
        this.addExternalOutputCoupling(actor.out_move_cmd,this.out_move_cmd);

        /**
         * IC
         */
        this.addInternalCoupling(updater.out_target_info,actor.in_target_info);

    }
}
