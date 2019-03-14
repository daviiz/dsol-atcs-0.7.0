package combatSysModel.DEM.atomicModel;

import devs.core.AtomicModelBase;
import combatSysModel.OM.Sensor_updater_om;
import combatSysModel.portType.move_result;
import combatSysModel.portType.request;
import combatSysModel.portType.response;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.OutputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class Updater_Sensor_am extends AtomicModelBase<Sensor_updater_om> {

    @Override
    protected void constructObjectModel() {
        this.om = new Sensor_updater_om();
    }

    public InputPort<Double, Double, SimTimeDouble, move_result> in_move_result;
    public InputPort<Double, Double, SimTimeDouble, request> in_request;

    public OutputPort<Double, Double, SimTimeDouble, response> out_response;

    private Phase UPDATE,REQUEST;


    public Updater_Sensor_am(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Updater_Sensor_am(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_move_result = new InputPort<Double, Double, SimTimeDouble, move_result>(this);
        in_request = new InputPort<Double, Double, SimTimeDouble, request>(this);
        out_response = new OutputPort<Double, Double, SimTimeDouble, response>(this);
    }

    @Override
    protected void constructPhase() {
        UPDATE = new Phase("UPDATE");  UPDATE.setLifeTime(Double.POSITIVE_INFINITY);
        REQUEST = new Phase("REQUEST"); REQUEST.setLifeTime(0.0);
        this.phase = UPDATE;
    }

    @Override
    protected void deltaExternalFunc(Object value) {
        if(this.phase.getName().equals(UPDATE.getName())){
            if(this.activePort == in_move_result){
                this.om.setIn_move_result((move_result)value);

                return;
            }
            if(this.activePort == in_request){
                this.om.setIn_request((request)value);
                this.phase = REQUEST;
                return;
            }
            return;
        }
    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase.getName().equals(REQUEST.getName())){
            this.om.data_integrator();
            this.phase = UPDATE;
            return;
        }
    }

    @Override
    protected void lambdaFunc() {
        if(this.phase.getName().equals(REQUEST.getName())){
            this.om.getOut_response().setSenderId(this.fullName);
            this.out_response.send(this.om.getOut_response());
            this.phase = UPDATE;
            return;
        }
    }
}
