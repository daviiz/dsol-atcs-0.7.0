package combatSysModel.DEM.coupledModel;

import devs.core.CoupledModelBase;
import combatSysModel.portType.move_result;
import combatSysModel.portType.scen_info;
import combatSysModel.portType.wp_guidance;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.OutputPort;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class Weapon_cm extends CoupledModelBase {

    /**
     * X:
     */
    public InputPort<Double, Double, SimTimeDouble, scen_info> in_entity_info;
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.engage_result> in_engage_result;
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.move_result> in_move_result;
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.env_info> in_env_info;
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.wp_guidance> in_wp_guidance;

    /**
     * Y:
     */
    public OutputPort<Double, Double, SimTimeDouble, move_result> out_move_result;
    public OutputPort<Double, Double, SimTimeDouble, combatSysModel.portType.wp_guidance> out_guidance_info;

    /**
     * component models
     */
    public Maneuver_cm m;
    public Sensor_cm s;
    public Controller_Weapon_cm c;

    public Weapon_cm(String modelName) {
        super(modelName);
    }

    public Weapon_cm(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Weapon_cm(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        /**
         * X
         */
        in_entity_info = new InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.scen_info>(this);
        in_engage_result = new InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.engage_result>(this);
        in_move_result = new InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.move_result>(this);
        in_env_info = new InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.env_info>(this);
        in_wp_guidance = new InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.wp_guidance>(this);

        /**
         * Y
         */
        out_move_result = new OutputPort<Double, Double, SimTimeDouble, move_result>(this);
        out_guidance_info = new OutputPort<Double, Double, SimTimeDouble, wp_guidance>(this);
    }

    @Override
    protected void couplingComponent() {
        /**
         *  { Mi }
         */
        m = new Maneuver_cm("Maneuver", this);
        s = new Sensor_cm("Sensor", this);
        c = new Controller_Weapon_cm("Controller", this);
        m.constructModel();
        s.constructModel();
        c.constructModel();

        /**
         * EIC
         */
        this.addExternalInputCoupling(this.in_move_result,s.in_move_result);
        this.addExternalInputCoupling(this.in_entity_info,s.in_scen_info);
        this.addExternalInputCoupling(this.in_entity_info,m.in_scen_info);
        this.addExternalInputCoupling(this.in_entity_info,c.in_scen_info);
        this.addExternalInputCoupling(this.in_env_info,s.in_env_info);
        this.addExternalInputCoupling(this.in_env_info,m.in_env_info);
        this.addExternalInputCoupling(this.in_engage_result,s.in_engage_result);
        this.addExternalInputCoupling(this.in_engage_result,m.in_engage_result);
        this.addExternalInputCoupling(this.in_engage_result,c.in_engage_result);
        this.addExternalInputCoupling(this.in_wp_guidance,c.in_wp_guidance);

        /**
         * EOC
         */
        this.addExternalOutputCoupling(m.out_wp_guidance,this.out_guidance_info);
        this.addExternalOutputCoupling(m.out_move_result,this.out_move_result);

        /**
         * IC
         */
        this.addInternalCoupling(s.out_threat_info,c.in_threat_info);
        this.addInternalCoupling(c.out_move_cmd,m.in_move_cmd);
        this.addInternalCoupling(m.out_move_finished,c.in_move_finished);
        this.addInternalCoupling(m.out_fuel_exhausted,s.in_fuel_exhausted);
        //this.addInternalCoupling(m.out_fuel_exhausted,c.in_fuel_exhausted);
    }
}
