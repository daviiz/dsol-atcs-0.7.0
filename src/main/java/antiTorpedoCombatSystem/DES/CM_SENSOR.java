package antiTorpedoCombatSystem.DES;

import antiTorpedoCombatSystem.portType.*;
import devs.core.CoupledModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class CM_SENSOR extends CoupledModelBase {

    public InputPortBase<simCtrl> in_simCtrl;
    public InputPortBase<sonarInfo> in_sonarInfo;
    public InputPortBase<ownPos> in_ownPos;

    public OutputPortBase<detect> out_detect;
    public OutputPortBase<sonarInfo> out_sonarInfo;

    public AM_TRANSMITTER_DES transmitter;
    public AM_RECEIVER_DES receiver;

    public CM_SENSOR(String modelName) {
        super(modelName);
    }

    public CM_SENSOR(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public CM_SENSOR(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_simCtrl = new InputPortBase<simCtrl>(this);
        in_sonarInfo = new InputPortBase<sonarInfo>(this);
        in_ownPos = new InputPortBase<ownPos>(this);

        out_detect = new OutputPortBase<detect>(this);
        out_sonarInfo = new OutputPortBase<sonarInfo>(this);
    }

    @Override
    protected void couplingComponent() {
        transmitter = new AM_TRANSMITTER_DES("transmitter",this);
        receiver = new AM_RECEIVER_DES("receiver",this);

        transmitter.constructModel();
        receiver.constructModel();

        /**
         * EIC
         */
        this.addExternalInputCoupling(this.in_ownPos,transmitter.in_ownPos);
        this.addExternalInputCoupling(this.in_simCtrl,receiver.in_simCtrl);
        this.addExternalInputCoupling(this.in_sonarInfo,receiver.in_sonarInfo);
        /**
         * EOC
         */
        this.addExternalOutputCoupling(receiver.out_detect,this.out_detect);
        this.addExternalOutputCoupling(transmitter.out_sonarInfo,this.out_sonarInfo);
        /**
         * IC
         */
        this.addInternalCoupling(transmitter.out_sonarInfo,receiver.in_sonarInfo);
    }
}
