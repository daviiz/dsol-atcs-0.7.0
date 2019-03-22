package asw_0_8_01.des.am;

import asw_0_8_01.om.PlatformControllerUpdaterOm;
import asw_0_8_01.portMsgType.scen_info;
import asw_0_8_01.portMsgType.threat_info;
import combatSysModel.portType.target_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class PlatformControllerUpdater extends AtomicModelBase<PlatformControllerUpdaterOm> {

    public InputPortBase<threat_info> in_threat_info;
    public InputPortBase<scen_info> in_scen_info;

    public OutputPortBase<target_info> out_target_info;


    public PlatformControllerUpdater(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformControllerUpdater(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_threat_info = new InputPortBase<threat_info>(this);
        in_scen_info = new InputPortBase<scen_info>(this);
        out_target_info = new OutputPortBase<target_info>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new PlatformControllerUpdaterOm();
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
