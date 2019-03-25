package asw_0_8_01.des.am;

import asw_0_8_01.om.PlatformSensorUpdaterOm;
import asw_0_8_01.portMsgType.move_result;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

import java.util.ArrayList;
import java.util.HashMap;

public class PlatformSensorUpdater extends AtomicModelBase<PlatformSensorUpdaterOm> {

    public InputPortBase<move_result> in_move_result;
    private move_result in_move_result_value;

    public InputPortBase<String> in_request;
    private String in_request_value;
    public OutputPortBase<HashMap<String,move_result>> out_response;

    HashMap<String,move_result> entitiesData;

    private Phase UPDATE,REQUEST;

    public PlatformSensorUpdater(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformSensorUpdater(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_move_result = new InputPortBase<move_result>(this);
        in_request = new InputPortBase<String>(this);
        out_response = new OutputPortBase<HashMap<String,move_result>>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new PlatformSensorUpdaterOm();
        entitiesData = new HashMap<String,move_result>();
        in_move_result_value = new move_result();
        in_request_value = "";
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
                in_move_result_value = (move_result)value;
                this.data_integrator((move_result)value);
                return;
            }
            if(this.activePort == in_request){
                in_request_value = (String)value;
                this.phase = REQUEST;
                return;
            }
            return;
        }
    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase == REQUEST){
            this.phase = UPDATE;
        }
    }

    @Override
    protected void lambdaFunc() {
        if(this.phase == REQUEST){
            this.out_response.send(entitiesData);
        }
    }

    private void  data_integrator(move_result value){
        if(value.getName().equals("0")){
            return ;
        }
        if(entitiesData.size()>0){
            if(entitiesData.containsKey(value.getName())){
                entitiesData.replace(value.getName(),value);
            }else{
                entitiesData.put(value.getName(),value);
            }
        }else{
            entitiesData.put(value.getName(),value);
        }
    }
}
