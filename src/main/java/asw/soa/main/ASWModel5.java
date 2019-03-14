package asw.soa.main;

import asw.soa.data.ViewData;
import asw.soa._DEM.coupledModel.RootCoupledModel;
import asw.soa.view.Visual2dService;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.language.d3.CartesianPoint;

import javax.naming.NamingException;
import java.rmi.RemoteException;

public class ASWModel5 extends AbstractDSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble> {

//    Fleet fleet;
//    Submarine sub;
//    Environment env;

    public ASWModel5(final DEVSSimulatorInterface.TimeDouble simulator) {
        super(simulator);
    }

    @Override
    public void constructModel() throws SimRuntimeException {

        //模型初始化：
        ViewData f1Data = new ViewData("Fleet");
        f1Data.origin = f1Data.destination = new CartesianPoint(-200, -50, 0);
        ViewData s1Data = new ViewData("Submarine");
        s1Data.origin = s1Data.destination = new CartesianPoint(200, 100, 0);

        RootCoupledModel root = new RootCoupledModel("root");
        root.setSimulator(this.simulator);
        root.initialize(f1Data, s1Data);

        try {
            Visual2dService.getInstance().register(f1Data.name, simulator, f1Data);
            Visual2dService.getInstance().register(s1Data.name, simulator, s1Data);

        } catch (NamingException e) {
            SimLogger.always().error(e);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
