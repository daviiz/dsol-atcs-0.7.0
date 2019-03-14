package combatSysModel.DEM.atomicModel;

import devs.core.AtomicModelBase;
import combatSysModel.OM.Sensor_actor_om;
import combatSysModel.portType.*;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.OutputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class Actor_Sensor_am extends AtomicModelBase<Sensor_actor_om> {
    @Override
    protected void constructObjectModel() {
        this.om = new Sensor_actor_om();
    }

    public InputPort<Double, Double, SimTimeDouble, engage_result> in_engage_result;
    public InputPort<Double,Double, SimTimeDouble, env_info> in_env_info;
    public InputPort<Double,Double, SimTimeDouble, scen_info> in_scen_info;
    public InputPort<Double, Double, SimTimeDouble, response> in_response;

    public OutputPort<Double, Double, SimTimeDouble, threat_info> out_threat_info;
    public OutputPort<Double, Double, SimTimeDouble, request> out_request;

    private Phase IDLE,PERIOD,REQUEST,DETECT;

    @Override
    protected void constructPort() {
        in_engage_result = new InputPort<Double, Double, SimTimeDouble, engage_result>(this);
        in_env_info = new InputPort<Double, Double, SimTimeDouble, env_info>(this);
        in_scen_info = new InputPort<Double, Double, SimTimeDouble, scen_info>(this);
        in_response = new InputPort<Double, Double, SimTimeDouble, response>(this);

        out_request = new OutputPort<Double, Double, SimTimeDouble, request>(this);
        out_threat_info = new OutputPort<Double, Double, SimTimeDouble, threat_info>(this);
    }

    @Override
    protected void constructPhase() {
        IDLE = new Phase("IDLE");   this.IDLE.setLifeTime(Double.POSITIVE_INFINITY);
        PERIOD = new Phase("PERIOD");   this.PERIOD.setLifeTime(10.0);
        REQUEST = new Phase("REQUEST");   this.REQUEST.setLifeTime(Double.POSITIVE_INFINITY);
        DETECT = new Phase("DETECT");   this.DETECT.setLifeTime(0.0);
        this.phase = IDLE;
    }

    @Override
    protected void deltaExternalFunc(Object value) {
        if(this.phase.getName().equals(IDLE.getName())){
            if(this.activePort == in_scen_info){
                this.om.setIn_scen_info((scen_info)value);
                this.phase = PERIOD;
            }
            return;
        }
        if(this.phase.getName().equals(PERIOD.getName())){
            if(this.activePort == in_engage_result){
                this.om.setIn_engage_result((engage_result)value);
                this.phase = IDLE;
                return;
            }
            if(this.activePort == in_env_info){
                //this.om.setIn_env_info((env_info)value);
                return;
            }
            return;
        }
        if(this.phase.getName().equals(DETECT.getName())){
            if(this.activePort == in_engage_result){
                this.om.setIn_engage_result((engage_result)value);
                this.phase = IDLE;
                return;
            }
            return;
        }
        if(this.phase.getName().equals(REQUEST.getName())){
            if(this.activePort == in_response){
                this.om.setIn_response((response)value);
                this.phase = DETECT;
                return;
            }
            if(this.activePort == in_env_info){
                this.om.setIn_env_info((env_info)value);
            }
            return;
        }
    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase.getName().equals(DETECT.getName())){
            this.om.detection_algrithm();
            return;
        }
    }

    @Override
    protected void lambdaFunc() {
        if(this.phase.getName().equals(DETECT.getName())){
            this.om.getOut_threat_info().setSenderId(this.fullName);
            this.out_threat_info.send(this.om.getOut_threat_info());
            this.phase = PERIOD;
            return;
        }
        if(this.phase.getName().equals(PERIOD.getName())){
            this.om.getOut_request().setSenderId(this.fullName);
            this.out_request.send(this.om.getOut_request());
            this.phase = REQUEST;
            return;
        }
    }

    public Actor_Sensor_am(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Actor_Sensor_am(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }
}
