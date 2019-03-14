package antiTorpedoCombatSystem.DES;

import antiTorpedoCombatSystem.OM.OM_ENV;
import antiTorpedoCombatSystem.portType.sonarInfo;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

import java.util.Iterator;
import java.util.Map;

public class AM_ENV extends AtomicModelBase<OM_ENV>{

    @Override
    protected void constructObjectModel() {
        this.om = new OM_ENV();
    }

    public InputPortBase<sonarInfo> in_sonarInfo;
    public OutputPortBase<sonarInfo> out_sonarInfo;

    private Phase INFINITY;

    @Override
    protected void constructPort() {
        in_sonarInfo = new InputPortBase<sonarInfo>(this);
        out_sonarInfo = new OutputPortBase<sonarInfo>(this);
    }

    @Override
    protected void constructPhase() {
        INFINITY = new Phase("INFINITY");
        INFINITY.setLifeTime(2);
        this.phase = INFINITY;
    }

    @Override
    protected  void deltaExternalFunc(Object value) {
        sonarInfo tmp = (sonarInfo) value;
        this.om.updateEnviroment(tmp);
    }

    @Override
    protected void deltaInternalFunc() {
        return;
    }

    @Override
    protected  void lambdaFunc() {
        if (this.om.getOut_sonarInfoMap().size()>0) {

            Iterator iter = this.om.getOut_sonarInfoMap().entrySet().iterator();
            while(iter.hasNext()) {
                Map.Entry entry = (Map.Entry)iter.next();
                // 获取key
                String key = (String)entry.getKey();
                // 获取value
                sonarInfo value = (sonarInfo)entry.getValue();

                Iterator iter2 = this.om.getOut_sonarInfoMap().keySet().iterator();
                while (iter2.hasNext()) {
                    String tmp = (String)iter2.next();
                    if(tmp.equals(key)){
                        continue;
                    }
                    else{
                        if(value != null){
                            value.setSenderId(this.fullName);
                            out_sonarInfo.send(value);
                        }
                    }
                }

            }
        }
    }

    public AM_ENV(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public AM_ENV(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }
}
