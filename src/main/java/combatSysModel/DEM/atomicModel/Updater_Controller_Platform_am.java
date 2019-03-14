package combatSysModel.DEM.atomicModel;

import devs.core.AtomicModelBase;
import combatSysModel.OM.Platform_Controller_updater_om;
import combatSysModel.portType.env_info;
import combatSysModel.portType.target_info;
import combatSysModel.portType.threat_info;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.OutputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class Updater_Controller_Platform_am extends AtomicModelBase<Platform_Controller_updater_om> {

    @Override
    protected void constructObjectModel() {
        this.om = new Platform_Controller_updater_om();
    }

    public InputPort<Double,Double, SimTimeDouble, threat_info> in_threat_info;
    public InputPort<Double,Double, SimTimeDouble, env_info> in_env_info;

    public OutputPort<Double,Double,SimTimeDouble, target_info> out_target_info;

    private Phase WAIT,IDENTIFICATION;

    @Override
    protected void constructPort() {
        in_threat_info = new InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.threat_info>(this);
        in_env_info = new InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.env_info>(this);
        out_target_info = new OutputPort<Double, Double, SimTimeDouble, target_info>(this);
    }

    @Override
    protected void constructPhase() {
        WAIT = new Phase("WAIT");
        WAIT.setLifeTime(Double.POSITIVE_INFINITY);
        IDENTIFICATION = new Phase("IDENTIFICATION");
        IDENTIFICATION.setLifeTime(15.0);
        this.phase = WAIT;
    }

    @Override
    protected void deltaExternalFunc(Object value) {
        if (this.phase.getName().equals("WAIT")) {
            if (this.activePort == in_env_info) {
                this.om.setEnv_info((env_info)value);
                return;
            }
            if (this.activePort == in_threat_info) {
                this.om.setThreat_info((threat_info)value);
                this.phase = IDENTIFICATION;
                return;
            }
            return;
        }
        if (this.phase.getName().equals("IDENTIFICATION")) {
            if (this.activePort == in_threat_info) {
                this.om.setThreat_info((threat_info)value);
                return;
            }
            return;
        }
    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase.getName().equals("IDENTIFICATION")){
            this.om.identification();
            return;
        }
    }

    @Override
    protected void lambdaFunc() {
        if(this.phase.getName().equals("IDENTIFICATION")){
            this.om.getTarget_info().setSenderId(this.fullName);
            out_target_info.send(this.om.getTarget_info());
            this.phase = WAIT;
        }
    }

    public Updater_Controller_Platform_am(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Updater_Controller_Platform_am(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }
}
