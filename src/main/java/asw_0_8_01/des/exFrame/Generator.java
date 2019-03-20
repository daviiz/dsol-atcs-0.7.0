package asw_0_8_01.des.exFrame;

import asw_0_8_01.om.GeneratorOm;
import asw_0_8_01.portMsgType.engage_result;
import asw_0_8_01.portMsgType.entity_gen;
import asw_0_8_01.portMsgType.scen_gen;
import asw_0_8_01.portMsgType.wp_launch;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class Generator extends AtomicModelBase<GeneratorOm> {


    public InputPortBase<wp_launch> in_wp_launch;
    public OutputPortBase<scen_gen> out_scen_info;
    public OutputPortBase<entity_gen> out_entity_info;

    @Override
    protected void constructPort() {

        in_wp_launch = new InputPortBase<wp_launch>(this);
        out_entity_info = new OutputPortBase<entity_gen>(this);
        out_scen_info = new OutputPortBase<scen_gen>(this);
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
