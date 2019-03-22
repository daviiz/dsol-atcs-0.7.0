package asw_0_8_01.des.am;

import asw_0_8_01.om.WeaponControllerUpdaterOm;
import asw_0_8_01.portMsgType.threat_info;
import combatSysModel.portType.target_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class WeaponControllerUpdater extends AtomicModelBase<WeaponControllerUpdaterOm> {

    public InputPortBase<threat_info> in_threat_info;
    public OutputPortBase<target_info> out_target_info;

    public WeaponControllerUpdater(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public WeaponControllerUpdater(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_threat_info = new InputPortBase<threat_info>(this);
        out_target_info = new OutputPortBase<target_info>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new WeaponControllerUpdaterOm();
    }

    @Override
    protected void constructPhase() {

    }

    @Override
    protected void deltaExternalFunc(Object value) {

    }

    @Override
    protected void deltaInternalFunc() {

    }

    @Override
    protected void lambdaFunc() {

    }
}
