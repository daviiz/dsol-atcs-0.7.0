package asw_0_8_01.des.cm;

import asw_0_8_01.portMsgType.*;
import devs.core.CoupledModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class Weapon extends CoupledModelBase {

    public InputPortBase<entity_info> in_entity_info;
    public InputPortBase<env_info> in_env_info;
    public InputPortBase<Boolean> in_engage_result;
    public InputPortBase<move_result> in_move_result;

    public OutputPortBase<move_result> out_move_result;

    private WeaponController controller;
    private WeaponSensor sensor;
    private WeaponManeuver maneuver;

    public Weapon(String modelName) {
        super(modelName);
    }

    public Weapon(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Weapon(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_entity_info = new InputPortBase<entity_info>(this);
        in_env_info = new InputPortBase<env_info>(this);
        in_engage_result = new InputPortBase<Boolean>(this);
        in_move_result = new InputPortBase<move_result>(this);

        out_move_result = new OutputPortBase<move_result>(this);
    }

    @Override
    protected void couplingComponent() {
        controller = new WeaponController("controller",this);
        sensor = new WeaponSensor("sensor",this);
        maneuver = new WeaponManeuver("maneuver",this);
        controller.constructModel();
        sensor.constructModel();
        maneuver.constructModel();

        /**
         * EIC:
         */
        this.addExternalInputCoupling(this.in_move_result,sensor.in_move_result);

        this.addExternalInputCoupling(this.in_entity_info,sensor.in_entity_info);
        this.addExternalInputCoupling(this.in_entity_info,maneuver.in_entity_info);
        this.addExternalInputCoupling(this.in_entity_info,controller.in_entity_info);

        this.addExternalInputCoupling(this.in_env_info,sensor.in_env_info);
        this.addExternalInputCoupling(this.in_env_info,maneuver.in_env_info);

        this.addExternalInputCoupling(this.in_engage_result,sensor.in_engage_result);
        this.addExternalInputCoupling(this.in_engage_result,maneuver.in_engage_result);
        this.addExternalInputCoupling(this.in_engage_result,controller.in_engage_result);

        /**
         * EOC:
         */
        this.addExternalOutputCoupling(maneuver.out_move_result,this.out_move_result);

        /**
         * IC
         */
        this.addInternalCoupling(sensor.out_threat_info,controller.in_threat_info);
        this.addInternalCoupling(controller.out_move_cmd,maneuver.in_move_cmd);
        this.addInternalCoupling(maneuver.out_move_finished,controller.in_move_finished);
        this.addInternalCoupling(maneuver.out_fuel_exhasuted,sensor.in_engage_result);
        this.addInternalCoupling(maneuver.out_fuel_exhasuted,controller.in_engage_result);

    }
}
