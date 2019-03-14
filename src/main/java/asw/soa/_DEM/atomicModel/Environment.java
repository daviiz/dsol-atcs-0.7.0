package asw.soa._DEM.atomicModel;

import asw.soa._DEM.portType.MoveResult;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.*;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.exceptions.PortAlreadyDefinedException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Environment extends AtomicModel<Double, Double, SimTimeDouble> {

    public InputPort<Double, Double, SimTimeDouble, MoveResult> in_MoveResult;
    public OutputPort<Double, Double, SimTimeDouble, MoveResult> out_MoveResult;

    private HashMap<String,MoveResult> result;

    private Phase INFINITY;

    public Environment(String modelName, CoupledModel<Double, Double, SimTimeDouble> parentModel) {
        super(modelName, parentModel);
        //this.conflictStrategy = false;
    }

    @Override
    public void initialize(Double e) {
        this.in_MoveResult = new InputPort<Double, Double, SimTimeDouble, MoveResult>(this);
        this.out_MoveResult = new OutputPort<Double, Double, SimTimeDouble, MoveResult>(this);
        INFINITY = new Phase("INFINITY");
        INFINITY.setLifeTime(2);

        result = new HashMap<String,MoveResult>();

        /**
         * 输入输出端口设置
         */
        try {
            this.addInputPort("in_MoveResult", in_MoveResult);
            this.addOutputPort("out_MoveResult", out_MoveResult);
        } catch (PortAlreadyDefinedException ex) {
            SimLogger.always().error(ex);
        }
        /**
         * 模型状态初始化：
         */
        this.phase = INFINITY;
        super.initialize(e);
    }

    @Override
    protected void deltaInternal() {
        this.elapsedTime = 0.0;
    }

    @Override
    protected synchronized void deltaExternal(Double e, Object value) {
        this.elapsedTime = this.elapsedTime + e;
        System.out.println("---" + this.modelName+" --Receive MSG"+", SimTime: " + this.simulator.getSimulatorTime());

        if (this.phase.getName().equals("INFINITY")) {
            MoveResult tmp = (MoveResult)value;
            if(result.containsKey(tmp.senderId)){
                result.remove(tmp.senderId);
                result.put(tmp.senderId,tmp);
            }else{
                result.put(tmp.senderId,tmp);
            }
        }
    }

    @Override
    protected void lambda() {

        System.out.println("---" + this.modelName+" --Send MSG"+", SimTime: " + this.simulator.getSimulatorTime());
        if (this.phase.getName().equals("INFINITY") && result.size()>0) {

            Iterator iter = result.entrySet().iterator();
            while(iter.hasNext()) {
                Map.Entry entry = (Map.Entry)iter.next();
                // 获取key
                String key = (String)entry.getKey();
                // 获取value
                MoveResult value = (MoveResult)entry.getValue();

                Iterator iter2 = result.keySet().iterator();
                while (iter2.hasNext()) {
                    String tmp = (String)iter2.next();
                    if(tmp.equals(key)){
                        continue;
                    }
                    else{
                        out_MoveResult.send(value);
                    }
                }

            }
        }
    }

    @Override
    protected Double timeAdvance() {
        return this.phase.getLifeTime();
    }
}
