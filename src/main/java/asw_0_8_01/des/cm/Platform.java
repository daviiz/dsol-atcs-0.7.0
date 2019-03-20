package asw_0_8_01.des.cm;

import asw_0_8_01.portMsgType.*;
import devs.core.CoupledModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class Platform extends CoupledModelBase {

    public InputPortBase<scen_info> in_scen_info;
    public InputPortBase<env_info> in_env_info;
    public InputPortBase<engage_result> in_engage_result;
    public InputPortBase<move_result> in_move_result;
    public InputPortBase<guidance_info> in_guidance_info;

    public OutputPortBase<move_result> out_move_result;
    public OutputPortBase<wp_launch> out_wp_launch;
    public OutputPortBase<wp_guidance> out_wp_guidance;

    private PlatformController controller;
    private PlatformSensor sensor;
    private PlatformManeuver maneuver;

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
        in_engage_result = new InputPortBase<engage_result>(this);
        in_move_result = new InputPortBase<move_result>(this);
        in_guidance_info = new InputPortBase<guidance_info>(this);

        out_move_result = new OutputPortBase<move_result>(this);
        out_wp_launch = new OutputPortBase<wp_launch>(this);
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


    }
}
