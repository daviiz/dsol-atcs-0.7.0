package asw.soa._DEM.coupledModel;

import asw.soa.data.ViewData;
import asw.soa._DEM.atomicModel.Environment;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

public class RootCoupledModel extends CoupledModel<Double, Double, SimTimeDouble> {

    Fleet fleet;
    Submarine sub;
    Environment env;

    public RootCoupledModel(String modelName) {
        super(modelName);
    }

    public void initialize(ViewData f1Data, ViewData s1Data) {
        sub = new Submarine("Submarine", this);
        sub.initialize(s1Data);
        fleet = new Fleet("Fleet", this);
        fleet.initialize(f1Data);
        env = new Environment("env", this);
        env.initialize(0.0);

        this.addInternalCoupling(fleet.out_ENT_INFO, env.in_MoveResult);
        this.addInternalCoupling(sub.out_ENT_INFO, env.in_MoveResult);
        this.addInternalCoupling(env.out_MoveResult, fleet.in_ENV_INFO);
        this.addInternalCoupling(env.out_MoveResult, sub.in_ENV_INFO);
    }
}
