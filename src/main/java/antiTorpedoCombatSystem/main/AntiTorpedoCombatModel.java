package antiTorpedoCombatSystem.main;

import antiTorpedoCombatSystem.DES.CM_Root;
import asw.soa.data.ViewData;
import asw.soa.view.Visual2dService;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.language.d3.CartesianPoint;

import javax.naming.NamingException;
import java.rmi.RemoteException;

public class AntiTorpedoCombatModel extends AbstractDSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble>{
    @Override
    public void constructModel() throws SimRuntimeException {

        //模型数据准备：
        ViewData f1Data = new ViewData("Fleet");
        f1Data.origin = f1Data.destination = new CartesianPoint(-200, -50, 0);
        f1Data.status = true;
        ViewData s1Data = new ViewData("Submarine");
        s1Data.origin = s1Data.destination = new CartesianPoint(200, 100, 0);
        s1Data.status = true;

        ViewData d1Data = new ViewData("Decoy"); d1Data.isActive = false;
        ViewData t1Data = new ViewData("Torpedo"); t1Data.isActive = false;
        ViewData[] mData = {f1Data,s1Data,d1Data,t1Data};

        /**
         * 模型初始化及构造
         */
        CM_Root root = new CM_Root("");
        root.setSimulator(this.simulator);
        root.constructModel();

        /**
         *  可视化组件注册：
         */
        try {
            root.fleet.maneuver.getOm().setViewData(mData[0]);
            root.submarine.maneuver.getOm().setViewData(mData[1]);
            root.decoy.maneuver.getOm().setViewData(mData[2]);
            root.torpedo.maneuver.getOm().setViewData(mData[3]);

            Visual2dService.getInstance().register(mData[0].name, simulator, mData[0]);
            Visual2dService.getInstance().register(mData[1].name, simulator, mData[1]);
            Visual2dService.getInstance().register(mData[2].name, simulator, mData[2]);
            Visual2dService.getInstance().register(mData[3].name, simulator, mData[3]);

        } catch (NamingException e) {
            SimLogger.always().error(e);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public AntiTorpedoCombatModel(DEVSSimulatorInterface.TimeDouble simulator) {
        super(simulator);
    }
}
