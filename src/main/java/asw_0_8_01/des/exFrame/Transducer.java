package asw_0_8_01.des.exFrame;

import asw_0_8_01.om.TransducerOm;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class Transducer extends AtomicModelBase<TransducerOm> {
    public InputPortBase<Boolean> in_engage_result;

    private int replicationCount = 0;

    private int survivalCount = 0;

    private Phase WAIT,STAT;

    @Override
    protected void constructPort() {
        in_engage_result = new InputPortBase<Boolean>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new TransducerOm();
    }

    @Override
    protected void constructPhase() {
        WAIT = new Phase("WAIT"); WAIT.setLifeTime(Double.POSITIVE_INFINITY);
        STAT = new Phase("STAT"); WAIT.setLifeTime(0.0);
        this.phase = STAT;
    }

    @Override
    protected void deltaExternalFunc(Object value) {
        if(this.phase.getName().equals(WAIT.getName())){
            Boolean tmp = (Boolean)value;
            replicationCount += 1;
            if(tmp){
                survivalCount +=1;
            }
            this.phase = STAT;
        }
    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase.getName().equals(STAT.getName()) && replicationCount > 0 ){
            /**
             * 统计拦截效率：
             */
            System.out.println("当前样本执行了："+replicationCount+" 次；"+"成功拦截:"+survivalCount+" 次；"+"拦截效率为："+(survivalCount/replicationCount));
            replicationCount += 1;
            this.phase  = WAIT;
        }
    }

    @Override
    protected void lambdaFunc() {

    }

    public Transducer(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Transducer(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }
}
