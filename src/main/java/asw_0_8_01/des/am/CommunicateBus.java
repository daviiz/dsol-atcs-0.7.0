package asw_0_8_01.des.am;

import asw_0_8_01.portMsgType.move_result;
import combatSysModel.portType.env_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.ObjectModelBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CommunicateBus extends AtomicModelBase<ObjectModelBase> {

    public InputPortBase<move_result> in_move_result;
    public OutputPortBase<move_result> out_move_result;

    private Map<String,move_result> result = new HashMap<String,move_result>();

    private Phase INFINITY;

    @Override
    protected void constructPort() {
        in_move_result = new InputPortBase<move_result>(this);
        out_move_result = new OutputPortBase<move_result>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new ObjectModelBase();
    }

    @Override
    protected void constructPhase() {
        INFINITY = new Phase("INFINITY");
        INFINITY.setLifeTime(2);
        this.phase = INFINITY;
    }

    @Override
    protected void deltaExternalFunc(Object value) {
        move_result tmp = (move_result)value;
        // convert move_result to env_info
        // ...
        if(tmp.getCmp() !=0){
            if(result.containsKey(tmp.getSenderId())){
                result.replace(tmp.getName(),tmp);
            }else{
                result.put(tmp.getName(),tmp);
            }
        }
    }

    @Override
    protected void deltaInternalFunc() {

    }

    @Override
    protected void lambdaFunc() {
        if (result.size()>0) {

            Iterator iter = result.entrySet().iterator();
            while(iter.hasNext()) {
                Map.Entry entry = (Map.Entry)iter.next();
                // 获取key
                String key = (String)entry.getKey();
                // 获取value
                move_result value = (move_result)entry.getValue();

                Iterator iter2 = result.keySet().iterator();
                while (iter2.hasNext()) {
                    String tmp = (String)iter2.next();
                    if(tmp.equals(key)){
                        continue;
                    }
                    else{
                        //value.setSenderId(tmp);
                        if(value != null){
                            value.setSenderId(this.fullName);
                            out_move_result.send(value);
                        }
                    }
                }

            }
        }
    }

    public CommunicateBus(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public CommunicateBus(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }
}
