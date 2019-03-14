package combatSysModel.DEM.coupledModel;

import devs.core.CoupledModelBase;
import combatSysModel.DEM.atomicModel.Actor_Sensor_am;
import combatSysModel.DEM.atomicModel.Updater_Sensor_am;
import combatSysModel.portType.engage_result;
import combatSysModel.portType.fuel_exhausted;
import combatSysModel.portType.threat_info;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.OutputPort;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

class Sensor_cm extends CoupledModelBase {

    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.scen_info> in_scen_info;
    public InputPort<Double, Double, SimTimeDouble, engage_result> in_engage_result;
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.move_result> in_move_result;
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.env_info> in_env_info;
    public InputPort<Double, Double, SimTimeDouble, fuel_exhausted> in_fuel_exhausted;

    public OutputPort<Double, Double, SimTimeDouble, threat_info> out_threat_info;

    public Actor_Sensor_am actor;
    public Updater_Sensor_am updater;

    public Sensor_cm(String modelName) {
        super(modelName);
    }

    public Sensor_cm(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Sensor_cm(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
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
        in_fuel_exhausted = new InputPort<Double, Double, SimTimeDouble, fuel_exhausted>(this);
        /**
         * Y
         */
        out_threat_info = new OutputPort<Double, Double, SimTimeDouble, threat_info>(this);
    }

    @Override
    protected void couplingComponent() {
        updater = new Updater_Sensor_am("Updater",this);
        actor = new Actor_Sensor_am("Actor",this);
        actor.constructModel();
        updater.constructModel();

        /**
         * EIC
         */
        this.addExternalInputCoupling(this.in_engage_result,actor.in_engage_result);
        this.addExternalInputCoupling(this.in_env_info,actor.in_env_info);
        this.addExternalInputCoupling(this.in_scen_info,actor.in_scen_info);
        this.addExternalInputCoupling(this.in_move_result,updater.in_move_result);

        /**
         * IC
         */
        this.addInternalCoupling(updater.out_response,actor.in_response);
        this.addInternalCoupling(actor.out_request,updater.in_request);
        /**
         * EOC
         */
        this.addExternalOutputCoupling(actor.out_threat_info,this.out_threat_info);

    }
}
