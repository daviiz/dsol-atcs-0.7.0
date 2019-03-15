package antiTorpedoCombatSystem.DES;

import antiTorpedoCombatSystem.portType.cmdMsg;
import antiTorpedoCombatSystem.portType.result;
import antiTorpedoCombatSystem.portType.simCtrl;
import antiTorpedoCombatSystem.portType.sonarInfo;
import devs.core.CoupledModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class CM_UNDERWATER_PLATFORM extends CoupledModelBase {

    public InputPortBase<simCtrl> in_simCtrl;
    public InputPortBase<sonarInfo> in_sonarInfo;

    public InputPortBase<cmdMsg> in_cmdMsg;

    public OutputPortBase<cmdMsg> out_cmdMsg;
    public OutputPortBase<sonarInfo> out_sonarInfo;
    public OutputPortBase<result> out_result;

    public AM_CONTROL_DES control;
    public AM_MANEUVER_DES maneuver;
    public CM_SENSOR sensor;

    public CM_UNDERWATER_PLATFORM(String modelName) {
        super(modelName);
    }

    public CM_UNDERWATER_PLATFORM(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public CM_UNDERWATER_PLATFORM(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_simCtrl = new InputPortBase<simCtrl>(this);
        in_sonarInfo = new InputPortBase<sonarInfo>(this);
        out_cmdMsg = new OutputPortBase<cmdMsg>(this);
        out_sonarInfo = new OutputPortBase<sonarInfo>(this);
        out_result = new OutputPortBase<result>(this);

        in_cmdMsg = new InputPortBase<cmdMsg>(this);

    }

    @Override
    protected void couplingComponent() {
        control = new AM_CONTROL_DES("control",this);
        maneuver = new AM_MANEUVER_DES("maneuver",this);
        sensor = new CM_SENSOR("sensor",this);

        control.constructModel();
        maneuver.constructModel();
        sensor.constructModel();

        /**
         * EIC
         */
        this.addExternalInputCoupling(this.in_simCtrl,sensor.in_simCtrl);
        this.addExternalInputCoupling(this.in_simCtrl,control.in_simCtrl);
        this.addExternalInputCoupling(this.in_simCtrl,maneuver.in_simCtrl);

        this.addExternalInputCoupling(this.in_sonarInfo,sensor.in_sonarInfo);

        this.addExternalInputCoupling(this.in_cmdMsg,maneuver.in_cmdMsg);


        /**
         * EOC
         */
        this.addExternalOutputCoupling(control.out_result,this.out_result);
        this.addExternalOutputCoupling(control.out_cmdMsg,this.out_cmdMsg);
        this.addExternalOutputCoupling(sensor.out_sonarInfo,this.out_sonarInfo);
        /**
         * IC
         */
        this.addInternalCoupling(sensor.out_detect,control.in_detect);
        this.addInternalCoupling(control.out_ctrlMsg,maneuver.in_ctrlMsg);
        this.addInternalCoupling(maneuver.out_ownPos,sensor.in_ownPos);
        this.addInternalCoupling(maneuver.out_ownPos,control.in_ownPos);
    }
}
