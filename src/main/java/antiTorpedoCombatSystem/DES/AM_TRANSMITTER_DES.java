package antiTorpedoCombatSystem.DES;

import antiTorpedoCombatSystem.OM.OM_SENSOR;
import antiTorpedoCombatSystem.portType.ownPos;
import antiTorpedoCombatSystem.portType.sonarInfo;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class AM_TRANSMITTER_DES extends AtomicModelBase<OM_SENSOR> {

    public InputPortBase<ownPos> in_ownPos;

    public OutputPortBase<sonarInfo> out_sonarInfo;

    private Phase WAIT,TRANSMIT;

    public AM_TRANSMITTER_DES(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public AM_TRANSMITTER_DES(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_ownPos = new InputPortBase<ownPos>(this);
        out_sonarInfo = new OutputPortBase<sonarInfo>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new OM_SENSOR();
    }

    @Override
    protected void constructPhase() {
        WAIT = new Phase("WAIT");   WAIT.setLifeTime(Double.POSITIVE_INFINITY);
        TRANSMIT = new Phase("TRANSMIT");   TRANSMIT.setLifeTime(this.om.getTTransmit());
        this.phase = WAIT;
    }

    @Override
    protected void deltaExternalFunc(Object value) {
        if(this.phase.getName().equals(WAIT.getName())){
            if(this.activePort == in_ownPos){
                this.om.setIn_ownPos((ownPos)value);
                this.om.updateTransmitterSonarInfo();
                this.phase = TRANSMIT;
            }
        }
    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase.getName().equals(TRANSMIT.getName())){
            if(this.activePort == in_ownPos){
                this.phase = WAIT;
            }
        }
    }

    @Override
    protected void lambdaFunc() {
        if(this.phase.getName().equals(TRANSMIT.getName())){
            if(this.activePort == in_ownPos){
                this.om.getOut_sonarInfo().setSenderId(this.fullName);
                this.out_sonarInfo.send(this.om.getOut_sonarInfo());
            }
        }
    }
}
