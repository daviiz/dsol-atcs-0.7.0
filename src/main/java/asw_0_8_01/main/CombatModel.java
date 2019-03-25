package asw_0_8_01.main;

import asw.soa.data.ViewData;
import asw.soa.view.Visual2dService;
import asw_0_8_01.des.cm.CombatSystem;
import asw_0_8_01.des.exFrame.Generator;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.language.d3.CartesianPoint;

import javax.naming.NamingException;
import java.rmi.RemoteException;

public class CombatModel extends AbstractDSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble>{
    @Override
    public void constructModel() throws SimRuntimeException {

        //模型数据准备：
        ViewData f1Data = new ViewData("Fleet");
        f1Data.origin = f1Data.destination = new CartesianPoint(-200, -50, 0);
        f1Data.status = true;
        ViewData s1Data = new ViewData("Submarine");
        s1Data.origin = s1Data.destination = new CartesianPoint(200, 100, 0);
        s1Data.status = true;

        ViewData d1Data = new ViewData("Decoy1"); d1Data.isActive = false;
        ViewData d2Data = new ViewData("Decoy2"); d1Data.isActive = false;
        ViewData t1Data = new ViewData("Torpedo"); t1Data.isActive = false;
        ViewData[] mData = {f1Data,s1Data,d1Data,d2Data,t1Data};

        /**
         * 模型初始化及构造
         */
        CombatSystem root = new CombatSystem("");
        root.setSimulator(this.simulator);
        root.constructModel();

        /**
         *  可视化组件注册：
         */
        try {
            root.model.fleet.maneuver.actor.setViewData(mData[0]);
            root.model.submarine.maneuver.actor.setViewData(mData[1]);

            //root.model.decoy1.maneuver.actor.setViewData(mData[2]);
            //root.model.decoy2.maneuver.actor.setViewData(mData[3]);
            //root.model.torpedo.maneuver.actor.setViewData(mData[4]);

            Visual2dService.getInstance().register(mData[0].name, simulator, mData[0]);
            Visual2dService.getInstance().register(mData[1].name, simulator, mData[1]);
            Visual2dService.getInstance().register(mData[2].name, simulator, mData[2]);
            Visual2dService.getInstance().register(mData[3].name, simulator, mData[3]);
            Visual2dService.getInstance().register(mData[4].name, simulator, mData[4]);

        } catch (NamingException e) {
            SimLogger.always().error(e);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public CombatModel(DEVSSimulatorInterface.TimeDouble simulator) {
        super(simulator);
    }
}
