package asw_0_8_01.des.am;

import asw_0_8_01.om.WeaponSensorUpdaterOm;
import devs.core.AtomicModelBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class WeaponSensorUpdater extends AtomicModelBase<WeaponSensorUpdaterOm> {
    public WeaponSensorUpdater(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public WeaponSensorUpdater(String modelName, DEVSSimulator.TimeDouble simulator) {
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
