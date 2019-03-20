package asw_0_8_01.des.exFrame;

import asw_0_8_01.om.GeneratorOm;
import asw_0_8_01.portMsgType.entity_info;
import asw_0_8_01.portMsgType.scen_info;
import asw_0_8_01.portMsgType.wp_launch;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class Generator extends AtomicModelBase<GeneratorOm> {


    public InputPortBase<wp_launch> in_wp_launch;
    public OutputPortBase<scen_info> out_scen_info;
    public OutputPortBase<entity_info> out_entity_info;

    @Override
    protected void constructPort() {

        in_wp_launch = new InputPortBase<wp_launch>(this);
        out_entity_info = new OutputPortBase<entity_info>(this);
        out_scen_info = new OutputPortBase<scen_info>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new GeneratorOm();
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

    public Generator(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Generator(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }
}
