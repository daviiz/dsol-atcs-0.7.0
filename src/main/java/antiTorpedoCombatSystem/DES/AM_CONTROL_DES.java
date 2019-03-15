package antiTorpedoCombatSystem.DES;

import antiTorpedoCombatSystem.OM.OM_CONTROL;
import antiTorpedoCombatSystem.portType.*;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class AM_CONTROL_DES extends AtomicModelBase<OM_CONTROL> {

    public InputPortBase<simCtrl> in_simCtrl;
    public InputPortBase<detect> in_detect;
    public InputPortBase<ownPos> in_ownPos;

    public OutputPortBase<result> out_result;
    public OutputPortBase<cmdMsg> out_cmdMsg;
    public OutputPortBase<ctrlMsg> out_ctrlMsg;

    private Phase WAIT,IDENTIFY,CONTROL;

    public AM_CONTROL_DES(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public AM_CONTROL_DES(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_simCtrl = new InputPortBase<simCtrl>(this);
        in_detect = new InputPortBase<detect>(this);
        in_ownPos = new InputPortBase<ownPos>(this);
        out_result = new OutputPortBase<result>(this);
        out_cmdMsg = new OutputPortBase<cmdMsg>(this);
        out_ctrlMsg = new OutputPortBase<ctrlMsg>(this);
    }
    @Override
    protected void constructObjectModel() {
        this.om = new OM_CONTROL();
    }

    @Override
    protected void constructPhase() {
        WAIT = new Phase("WAIT"); WAIT.setLifeTime(Double.POSITIVE_INFINITY);
        IDENTIFY = new Phase("IDENTIFY"); IDENTIFY.setLifeTime(Double.POSITIVE_INFINITY);
        CONTROL = new Phase("CONTROL"); CONTROL.setLifeTime(this.om.getTControl());
        this.phase = IDENTIFY;
    }

    @Override
    protected void deltaExternalFunc(Object value) {
        int activePortCode = 0;
        /**
         * 接收输入：
         */
        if(this.activePort == in_simCtrl){
            this.om.setIn_simCtrl((simCtrl) value);
            activePortCode = 1;
        }
        if(this.activePort == in_detect){
            this.om.setIn_detect((detect) value);
            activePortCode = 2;
        }
        if(this.activePort == in_ownPos){
            this.om.setIn_ownPos((ownPos) value);
            activePortCode = 3;
        }


        /**
         * 外部状态转换及业务计算：
         */
        if(this.phase.getName().equals(WAIT.getName())){
            if(activePortCode == 2){
                this.phase = IDENTIFY;
            }
        }
        if(this.phase.getName().equals(IDENTIFY.getName())){
            /**
             *
             */
            this.om.Identification();
            if(activePortCode == 1){
                this.phase = WAIT;
            }
            if(activePortCode == 2){
                this.phase = CONTROL;
            }
        }
        if(this.phase.getName().equals(CONTROL.getName())){
            if(activePortCode == 1){
                this.phase = WAIT;
            }
        }
    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase.getName().equals(CONTROL.getName())){
            this.nextPhaseName = this.om.battlePlanning();

            if(this.nextPhaseName.equals(IDENTIFY.getName())){
                this.phase = IDENTIFY;

            }else if(this.nextPhaseName.equals(WAIT.getName())){
                this.phase = WAIT;

            }
        }
    }

    @Override
    protected void lambdaFunc() {
        if(this.phase.getName().equals(CONTROL.getName())){
            if(this.nextPhaseName.equals(IDENTIFY.getName())){
                this.om.getOut_cmdMsg().setSenderId(this.fullName);
                this.out_cmdMsg.send(this.om.getOut_cmdMsg());
                this.om.getOut_ctrlMsg().setSenderId(this.fullName);
                this.out_ctrlMsg.send(this.om.getOut_ctrlMsg());
            }else if(this.nextPhaseName.equals(WAIT.getName())){
                this.om.getOut_result().setSenderId(this.fullName);
                this.out_result.send(this.om.getOut_result());
            }
        }
    }
}
