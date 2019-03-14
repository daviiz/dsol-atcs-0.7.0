package combatSysModel.DEM.atomicModel;

import devs.core.AtomicModelBase;
import combatSysModel.OM.Environment_om;
import combatSysModel.portType.env_info;
import combatSysModel.portType.move_result;
import combatSysModel.portType.scen_info;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.OutputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Environment_am extends AtomicModelBase<Environment_om>{

    @Override
    protected void constructObjectModel() {
        this.om = new Environment_om();
    }

    public InputPort<Double,Double, SimTimeDouble, scen_info> in_scen_info;
    public InputPort<Double,Double, SimTimeDouble, move_result> in_move_result;
    public OutputPort<Double,Double, SimTimeDouble, env_info> out_env_info;

    private Map<String,env_info> result = new HashMap<String,env_info>();

    private Phase INFINITY;

//    private env_info env_info;

    @Override
    protected void constructPort() {
        in_scen_info = new InputPort<Double, Double, SimTimeDouble, scen_info>(this);
        out_env_info = new OutputPort<Double, Double, SimTimeDouble, env_info>(this);
        in_move_result = new InputPort<Double, Double, SimTimeDouble, move_result>(this);
    }

    @Override
    protected void constructPhase() {
        INFINITY = new Phase("INFINITY");
        INFINITY.setLifeTime(2);
        this.phase = INFINITY;
    }

    @Override
    protected  void deltaExternalFunc(Object value) {
        if (this.phase.getName().equals("INFINITY")) {
            move_result tmp = (move_result)value;
            // convert move_result to env_info
            // ...
            if(tmp.camp !=0){
                env_info env_info = new env_info();
                env_info.location = tmp.location;
                env_info.entityName = (tmp.getSenderId());
                env_info.camp = tmp.camp;
                //SimLogger.always().error("===================From {} - to {} ,: x:{} - y:{} ",tmp.getSenderId(),this.fullName, tmp.location.x,tmp.location.y);
                if(result.containsKey(tmp.getSenderId())){
                    result.remove(tmp.getSenderId());
                    result.put(tmp.getSenderId(),env_info);
                }else{
                    result.put(tmp.getSenderId(),env_info);
                }
            }
        }
    }

    @Override
    protected void deltaInternalFunc() {

    }

    @Override
    protected  void lambdaFunc() {
        if (this.phase.getName().equals("INFINITY") && result.size()>0) {

            Iterator iter = result.entrySet().iterator();
            while(iter.hasNext()) {
                Map.Entry entry = (Map.Entry)iter.next();
                // 获取key
                String key = (String)entry.getKey();
                // 获取value
                env_info value = (env_info)entry.getValue();

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
                            out_env_info.send(value);
                        }
                    }
                }

            }
        }
    }

    public Environment_am(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Environment_am(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }
}
