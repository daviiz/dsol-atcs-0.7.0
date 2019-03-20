package asw_0_8_01.des.am;

import asw_0_8_01.om.PlatformSensorUpdaterOm;
import devs.core.AtomicModelBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class PlatformSensorUpdater extends AtomicModelBase<PlatformSensorUpdaterOm> {
    public PlatformSensorUpdater(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformSensorUpdater(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

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
}
