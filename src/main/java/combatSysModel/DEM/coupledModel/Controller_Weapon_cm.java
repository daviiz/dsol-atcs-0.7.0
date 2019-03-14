package combatSysModel.DEM.coupledModel;

import devs.core.CoupledModelBase;
import combatSysModel.DEM.atomicModel.Actor_Controller_Weapon_am;
import combatSysModel.DEM.atomicModel.Updater_Controller_Weapon_am;
import combatSysModel.portType.*;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.OutputPort;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class Controller_Weapon_cm extends CoupledModelBase {

    public InputPort<Double, Double, SimTimeDouble, engage_result> in_engage_result;
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.scen_info> in_scen_info;
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.threat_info> in_threat_info;
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.move_finished> in_move_finished;
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.wp_guidance> in_wp_guidance;

    public OutputPort<Double, Double, SimTimeDouble, combatSysModel.portType.move_cmd> out_move_cmd;

    public Actor_Controller_Weapon_am actor;
    public Updater_Controller_Weapon_am updater;

    public Controller_Weapon_cm(String modelName) {
        super(modelName);
    }

    public Controller_Weapon_cm(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Controller_Weapon_cm(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        /**
         * X
         */
        in_engage_result = new InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.engage_result>(this);
        in_scen_info = new InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.scen_info>(this);
        in_threat_info = new InputPort<Double, Double, SimTimeDouble, threat_info>(this);
        in_move_finished = new InputPort<Double, Double, SimTimeDouble, move_finished>(this);
        in_wp_guidance = new InputPort<Double, Double, SimTimeDouble, wp_guidance>(this);
        /**
         * Y
         */
        out_move_cmd = new OutputPort<Double, Double, SimTimeDouble, move_cmd>(this);
    }

    @Override
    protected void couplingComponent() {
        actor = new Actor_Controller_Weapon_am("Actor",this);
        updater = new Updater_Controller_Weapon_am("Updater",this);
        actor.constructModel();
        updater.constructModel();

        /**
         * EIC
         */
        this.addExternalInputCoupling(this.in_move_finished,actor.in_move_finished);
        this.addExternalInputCoupling(this.in_engage_result,actor.in_engage_result);
        this.addExternalInputCoupling(this.in_scen_info,actor.in_scen_info);
        this.addExternalInputCoupling(this.in_scen_info,updater.in_scen_info);
        this.addExternalInputCoupling(this.in_threat_info,updater.in_threat_info);
        this.addExternalInputCoupling(this.in_wp_guidance,actor.in_wp_guidance);


        /**
         * IC
         */
        this.addInternalCoupling(updater.out_target_info,actor.in_target_info);


        /**
         * EOC
         */
        this.addExternalOutputCoupling(actor.out_move_cmd,this.out_move_cmd);

    }
}
