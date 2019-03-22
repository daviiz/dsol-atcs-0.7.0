package asw_0_8_01.des.cm;

import asw_0_8_01.des.am.WeaponSensorActor;
import asw_0_8_01.des.am.WeaponSensorUpdater;
import asw_0_8_01.portMsgType.*;
import devs.core.CoupledModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class WeaponSensor  extends CoupledModelBase {

    public InputPortBase<move_result> in_move_result;
    public InputPortBase<entity_info> in_entity_info;
    public InputPortBase<env_info> in_env_info;
    public InputPortBase<Boolean> in_engage_result;

    public OutputPortBase<threat_info> out_threat_info;

    private WeaponSensorActor actor;
    private WeaponSensorUpdater updater;

    public WeaponSensor(String modelName) {
        super(modelName);
    }

    public WeaponSensor(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public WeaponSensor(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_move_result = new InputPortBase<move_result>(this);
        in_entity_info = new InputPortBase<entity_info>(this);
        in_env_info = new InputPortBase<env_info>(this);
        in_engage_result = new InputPortBase<Boolean>(this);

        out_threat_info = new OutputPortBase<threat_info>(this);
    }

    @Override
    protected void couplingComponent() {
        actor = new WeaponSensorActor("actor",this);
        updater = new WeaponSensorUpdater("updater",this);
        actor.constructModel();
        updater.constructModel();
        /**
         * EIC
         */
        this.addExternalInputCoupling(this.in_move_result,updater.in_move_result);
        this.addExternalInputCoupling(this.in_entity_info,actor.in_entity_info);
        this.addExternalInputCoupling(this.in_env_info,actor.in_env_info);
        this.addExternalInputCoupling(this.in_engage_result,actor.in_engage_result);

        /**
         * EOC
         */
        this.addExternalOutputCoupling(actor.out_threat_info,this.out_threat_info);

        /**
         * IC
         */
        this.addInternalCoupling(updater.out_response,actor.in_response);
        this.addInternalCoupling(actor.out_request,updater.in_request);



    }
}
