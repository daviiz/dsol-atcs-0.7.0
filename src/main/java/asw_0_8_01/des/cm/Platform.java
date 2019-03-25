package asw_0_8_01.des.cm;

import asw_0_8_01.portMsgType.*;
import devs.core.CoupledModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class Platform extends CoupledModelBase {

    public InputPortBase<scen_info> in_scen_info;
    public InputPortBase<env_info> in_env_info;
    public InputPortBase<Boolean> in_engage_result;
    public InputPortBase<move_result> in_move_result;
    public InputPortBase<guidance_info> in_guidance_info;

    public OutputPortBase<move_result> out_move_result;
    public OutputPortBase<String> out_wp_launch;
    public OutputPortBase<wp_guidance> out_wp_guidance;

    public PlatformController controller;
    public PlatformSensor sensor;
    public PlatformManeuver maneuver;

    public Platform(String modelName) {
        super(modelName);
    }

    public Platform(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Platform(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_scen_info = new InputPortBase<scen_info>(this);
        in_env_info = new InputPortBase<env_info>(this);
        in_engage_result = new InputPortBase<Boolean>(this);
        in_move_result = new InputPortBase<move_result>(this);
        in_guidance_info = new InputPortBase<guidance_info>(this);

        out_move_result = new OutputPortBase<move_result>(this);
        out_wp_launch = new OutputPortBase<String>(this);
        out_wp_guidance = new OutputPortBase<wp_guidance>(this);
    }

    @Override
    protected void couplingComponent() {
        controller = new PlatformController("controller",this);
        sensor = new PlatformSensor("sensor",this);
        maneuver = new PlatformManeuver("maneuver",this);

        controller.constructModel();
        sensor.constructModel();
        maneuver.constructModel();

        /**
         * EIC:
         */
        this.addExternalInputCoupling(this.in_move_result,sensor.in_move_result);

        this.addExternalInputCoupling(this.in_scen_info,sensor.in_scen_info);
        this.addExternalInputCoupling(this.in_scen_info,maneuver.in_scen_info);
        this.addExternalInputCoupling(this.in_scen_info,controller.in_scen_info);

        this.addExternalInputCoupling(this.in_env_info,sensor.in_env_info);
        this.addExternalInputCoupling(this.in_env_info,maneuver.in_env_info);

        this.addExternalInputCoupling(this.in_engage_result,sensor.in_engage_result);
        this.addExternalInputCoupling(this.in_engage_result,maneuver.in_engage_result);
        this.addExternalInputCoupling(this.in_engage_result,controller.in_engage_result);

        /**
         * EOC:
         */
        this.addExternalOutputCoupling(maneuver.out_move_result,this.out_move_result);
        this.addExternalOutputCoupling(controller.out_wp_launch,this.out_wp_launch);
        this.addExternalOutputCoupling(controller.out_wp_launch,this.out_wp_launch);

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
