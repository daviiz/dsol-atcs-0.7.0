package asw_0_8_01.des.exFrame;

import asw_0_8_01.om.TransducerOm;
import devs.core.AtomicModelBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class Transducer extends AtomicModelBase<TransducerOm> {
    @Override
    protected void constructPort() {

    }

    @Override
    protected void constructObjectModel() {

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
