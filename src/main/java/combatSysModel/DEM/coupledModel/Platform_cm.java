package combatSysModel.DEM.coupledModel;

import devs.core.CoupledModelBase;
import combatSysModel.portType.move_result;
import combatSysModel.portType.wp_guidance;
import combatSysModel.portType.wp_launch;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.OutputPort;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class Platform_cm extends CoupledModelBase {

    /**
     * X:
     */
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.scen_info> in_scen_info;
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.engage_result> in_engage_result;
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.move_result> in_move_result;
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.env_info> in_env_info;
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.guidance_info> in_guidance_info;

    /**
     * Y:
     */
    public OutputPort<Double, Double, SimTimeDouble, combatSysModel.portType.move_result> out_move_result;
    public OutputPort<Double, Double, SimTimeDouble, combatSysModel.portType.wp_launch> out_wp_launch;
    public OutputPort<Double, Double, SimTimeDouble, combatSysModel.portType.wp_guidance> out_wp_guidance;

    /**
     * component models
     */
    public Maneuver_cm maneuver;
    public Sensor_cm sensor;
    public Controller_Platform_cm controller;

    public Platform_cm(String modelName) { super(modelName); }

    public Platform_cm(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Platform_cm(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        /**
         * X
         */
        in_scen_info = new InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.scen_info>(this);
        in_engage_result = new InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.engage_result>(this);
        in_move_result = new InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.move_result>(this);
        in_env_info = new InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.env_info>(this);
        in_guidance_info = new InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.guidance_info>(this);

        /**
         * Y
         */
        out_move_result = new OutputPort<Double, Double, SimTimeDouble, move_result>(this);
        out_wp_launch = new OutputPort<Double, Double, SimTimeDouble, wp_launch>(this);
        out_wp_guidance = new OutputPort<Double, Double, SimTimeDouble, wp_guidance>(this);
    }

    @Override
    protected void couplingComponent() {
        /**
         *  { Mi }
         */
        maneuver = new Maneuver_cm("Maneuver", this);
        maneuver.constructModel();
        sensor = new Sensor_cm("Sensor", this);
        sensor.constructModel();
        controller = new Controller_Platform_cm("Controller", this);
        controller.constructModel();
        /**
         * EIC
         */
        this.addExternalInputCoupling(this.in_scen_info, sensor.in_scen_info);
        this.addExternalInputCoupling(this.in_scen_info, maneuver.in_scen_info);
        this.addExternalInputCoupling(this.in_engage_result, maneuver.in_engage_result);
        this.addExternalInputCoupling(this.in_engage_result, sensor.in_engage_result);
        this.addExternalInputCoupling(this.in_engage_result, controller.in_engage_result);
        this.addExternalInputCoupling(this.in_move_result, sensor.in_move_result);
        this.addExternalInputCoupling(this.in_env_info, maneuver.in_env_info);
        this.addExternalInputCoupling(this.in_env_info, sensor.in_env_info);
        this.addExternalInputCoupling(this.in_env_info, controller.in_env_info);
        this.addExternalInputCoupling(this.in_guidance_info, maneuver.in_guidance_info);

        /**
         * EOC
         */
        this.addExternalOutputCoupling(maneuver.out_move_result, this.out_move_result);
        this.addExternalOutputCoupling(controller.out_wp_launch, this.out_wp_launch);
        this.addExternalOutputCoupling(maneuver.out_wp_guidance, this.out_wp_guidance);

        /**
         * IC
         */
        this.addInternalCoupling(sensor.out_threat_info, controller.in_threat_info);
        this.addInternalCoupling(controller.out_move_cmd, maneuver.in_move_cmd);
        this.addInternalCoupling(maneuver.out_move_finished, controller.in_move_finished);
        this.addInternalCoupling(maneuver.out_fuel_exhausted, sensor.in_fuel_exhausted);
        this.addInternalCoupling(maneuver.out_fuel_exhausted, controller.in_fuel_exhausted);

        this.addInternalCoupling(maneuver.out_move_result, controller.in_move_result);
    }
}
