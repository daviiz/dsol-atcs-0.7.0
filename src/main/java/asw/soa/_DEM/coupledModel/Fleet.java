package asw.soa._DEM.coupledModel;

import asw.soa._DEM.portType.MoveResult;
import asw.soa.data.ViewData;
import asw.soa._DEM.atomicModel.Controller;
import asw.soa._DEM.atomicModel.Maneuver;
import asw.soa._DEM.atomicModel.Sensor;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.OutputPort;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

public class Fleet extends CoupledModel<Double, Double, SimTimeDouble> {

    public InputPort<Double, Double, SimTimeDouble, MoveResult> in_ENV_INFO;
    public OutputPort<Double, Double, SimTimeDouble, MoveResult> out_ENT_INFO;


    private Sensor s;
    private Maneuver m;
    private Controller c;


    public Fleet(String modelName, CoupledModel<Double, Double, SimTimeDouble> parentModel) {
        super(modelName, parentModel);
    }

    public void initialize(ViewData data) {
        in_ENV_INFO = new InputPort<Double, Double, SimTimeDouble, MoveResult>(this);
        out_ENT_INFO = new OutputPort<Double, Double, SimTimeDouble, MoveResult>(this);


        s = new Sensor(data.name + "$sensor", this, data.detectRange);
        s.initialize(0.0);
        c = new Controller(data.name + "$controller", this);
        c.initialize(0.0);
        m = new Maneuver(data.name + "$maneuver", this, data);
        m.initialize(0.0);

        this.addExternalInputCoupling(this.in_ENV_INFO, s.in_THREAT_ENT_INFO);

        this.addInternalCoupling(m.out_MOVE_RESULT, s.in_MOVE_RESULT);
        this.addInternalCoupling(m.out_MOVE_RESULT, c.in_MOVE_RESULT);
        this.addInternalCoupling(s.out_THREAT_INFO, c.in_THREAT_INFO);
        this.addInternalCoupling(c.out_MOVE_CMD, m.in_MOVE_CMD);

        this.addExternalOutputCoupling(m.out_MOVE_RESULT, this.out_ENT_INFO);
    }

}
