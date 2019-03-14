package antiTorpedoCombatSystem.DES;

import antiTorpedoCombatSystem.OM.OM_SENSOR;
import antiTorpedoCombatSystem.portType.detect;
import antiTorpedoCombatSystem.portType.simCtrl;
import antiTorpedoCombatSystem.portType.sonarInfo;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class AM_RECEIVER_DES extends AtomicModelBase<OM_SENSOR> {

    public InputPortBase<simCtrl> in_simCtrl;
    public InputPortBase<sonarInfo> in_sonarInfo;
    public OutputPortBase<detect> out_detect;

    private Phase WAIT,RECEIVE,DETECT;

    public AM_RECEIVER_DES(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public AM_RECEIVER_DES(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_simCtrl = new InputPortBase<simCtrl>(this);
        in_sonarInfo = new InputPortBase<sonarInfo>(this);

        out_detect = new OutputPortBase<detect>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new OM_SENSOR();
    }

    @Override
    protected void constructPhase() {
        WAIT = new Phase("WAIT"); WAIT.setLifeTime(Double.POSITIVE_INFINITY);
        RECEIVE = new Phase("RECEIVE"); RECEIVE.setLifeTime(Double.POSITIVE_INFINITY);
        DETECT = new Phase("DETECT"); DETECT.setLifeTime(this.om.getTDetect());
        this.phase = RECEIVE;
    }

    @Override
    protected void deltaExternalFunc(Object value) {
        int activePortCode = 0;
        if(this.activePort == in_simCtrl){
            this.om.setIn_simCtrl((simCtrl) value);
            activePortCode = 1;
        }
        if(this.activePort == in_sonarInfo){
            this.om.setIn_sonarInfo((sonarInfo) value);
            activePortCode = 2;
        }

        if(this.phase.getName().equals(WAIT.getName()) && activePortCode == 1){
            this.phase = RECEIVE;
        }
        else if(this.phase.getName().equals(RECEIVE.getName()) && activePortCode == 1){
            this.phase = WAIT;
        }
        else if(this.phase.getName().equals(RECEIVE.getName()) && activePortCode == 2){
            this.om.updateInfo();
            this.phase = DETECT;
        }
        else if(this.phase.getName().equals(DETECT.getName()) && activePortCode == 2){
            this.om.updateInfo();
        }
        else if(this.phase.getName().equals(DETECT.getName()) && activePortCode == 1){
            this.phase = WAIT;
        }

    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase.getName().equals(DETECT.getName())){
            this.om.targetDetection();
            this.phase = RECEIVE;
        }
    }

    @Override
    protected void lambdaFunc() {
        if(this.phase.getName().equals(DETECT.getName())){
            this.om.getOut_detect().setSenderId(this.fullName);
            this.out_detect.send(this.om.getOut_detect());
        }
    }
}
