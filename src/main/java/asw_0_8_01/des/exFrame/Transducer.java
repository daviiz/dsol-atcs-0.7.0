package asw_0_8_01.des.exFrame;

import asw_0_8_01.om.TransducerOm;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class Transducer extends AtomicModelBase<TransducerOm> {
    public InputPortBase<Boolean> in_engage_result;

    @Override
    protected void constructPort() {
        in_engage_result = new InputPortBase<Boolean>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new TransducerOm();
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

    public Transducer(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Transducer(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }
}
