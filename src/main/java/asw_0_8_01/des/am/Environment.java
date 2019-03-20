package asw_0_8_01.des.am;

import asw_0_8_01.om.EnvironmentOm;
import asw_0_8_01.portMsgType.env_info;
import asw_0_8_01.portMsgType.scen_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class Environment extends AtomicModelBase<EnvironmentOm> {

    public InputPortBase<scen_info> in_scen_info;
    public OutputPortBase<env_info> out_env_info;

    public Environment(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Environment(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_scen_info = new InputPortBase<scen_info>(this);
        out_env_info = new OutputPortBase<env_info>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new EnvironmentOm();
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
