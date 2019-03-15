package antiTorpedoCombatSystem.DES;

import antiTorpedoCombatSystem.OM.OM_MANEUVER;
import antiTorpedoCombatSystem.portType.ctrlMsg;
import antiTorpedoCombatSystem.portType.ownPos;
import antiTorpedoCombatSystem.portType.simCtrl;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class AM_MANEUVER_DES extends AtomicModelBase<OM_MANEUVER> {

    public InputPortBase<ctrlMsg> in_ctrlMsg;
    public InputPortBase<simCtrl> in_simCtrl;

    public OutputPortBase<ownPos> out_ownPos;

    private Phase WAIT,MOVE;

    public AM_MANEUVER_DES(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public AM_MANEUVER_DES(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_ctrlMsg = new InputPortBase<ctrlMsg>(this);
        in_simCtrl = new InputPortBase<simCtrl>(this);

        out_ownPos = new OutputPortBase<ownPos>(this);

    }

    @Override
    protected void constructObjectModel() {
        this.om = new OM_MANEUVER();
    }

    @Override
    protected void constructPhase() {
        WAIT = new Phase("WAIT"); WAIT.setLifeTime(Double.POSITIVE_INFINITY);
        MOVE = new Phase("MOVE"); MOVE.setLifeTime(this.om.getTMove());
        this.phase = MOVE;
    }

    @Override
    protected void deltaExternalFunc(Object value) {
        int activePortCode = 0;
        if(this.activePort == in_ctrlMsg){
            activePortCode = 1;
            this.om.setIn_ctrlMsg((ctrlMsg)value);
        }
        if(this.activePort == in_simCtrl){
            activePortCode = 2;
            this.om.setIn_simCtrl((simCtrl) value);
        }

        if(this.phase.getName().equals(WAIT.getName()) && activePortCode == 1){
            this.phase = MOVE;
        }
        if(this.phase.getName().equals(MOVE.getName()) && activePortCode == 2){
            this.phase = WAIT;
        }
    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase.getName().equals(MOVE.getName())){
            this.om.CalculationNextPosition();
            this.om.getViewData().startTime = this.simulator.getSimulatorTime();
            this.om.getViewData().stopTime = this.om.getViewData().startTime + this.phase.getLifeTime();
        }
    }

    @Override
    protected void lambdaFunc() {
        if(this.phase.getName().equals(MOVE.getName())){
            this.om.getOut_ownPos().setSenderId(this.fullName);
            this.out_ownPos.send(this.om.getOut_ownPos());
        }
    }


}
