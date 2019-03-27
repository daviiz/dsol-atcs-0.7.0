package asw_0_8_01.des.am;

import asw_0_8_01.om.WeaponSensorUpdaterOm;
import asw_0_8_01.portMsgType.move_result;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WeaponSensorUpdater extends AtomicModelBase<WeaponSensorUpdaterOm> {

    public InputPortBase<move_result> in_move_result;

    public InputPortBase<String> in_request;
    public OutputPortBase<move_result[]> out_response;

    private HashMap<String,move_result> entitiesData;
    private move_result in_move_result_value;
    private String in_request_value;
    private boolean data_integratorFlag = false;

    private Phase UPDATE,REQUEST;

    public WeaponSensorUpdater(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public WeaponSensorUpdater(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_move_result = new InputPortBase<move_result>(this);
        in_request = new InputPortBase<String>(this);
        out_response = new OutputPortBase<move_result[]>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new WeaponSensorUpdaterOm();
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
                data_integratorFlag = true;
                return;
            }
            return;
        }
    }
    private void data_integrator(move_result value){
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
        data_integratorFlag = true;
    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase == REQUEST){
            this.phase = UPDATE;
        }
    }

    @Override
    protected void lambdaFunc() {
        if(data_integratorFlag){

            move_result[] results = new move_result[10];
            int i = 0;
            Iterator it = entitiesData.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry entry = (Map.Entry)it.next();
                String name = (String)entry.getKey();
                move_result value = (move_result)entry.getValue();
                results[i++] = value;

            }
            this.out_response.send(results);
            data_integratorFlag = false;

        }
    }
}
