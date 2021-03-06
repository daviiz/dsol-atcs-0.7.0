package asw_0_8_01.des.am;

import asw.soa.data.ViewData;
import asw.soa.util.SimUtil;
import asw.soa.view.Visual2dService;
import asw_0_8_01.om.PlatformManeuverActorOm;
import asw_0_8_01.portMsgType.env_info;
import asw_0_8_01.portMsgType.move_result;
import asw_0_8_01.portMsgType.scen_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.language.d3.CartesianPoint;

import javax.naming.NamingException;
import java.rmi.RemoteException;

public class PlatformManeuverActor extends AtomicModelBase<PlatformManeuverActorOm> {

    public InputPortBase<Boolean> in_engage_result;
    public InputPortBase<env_info> in_env_info;
    public InputPortBase<scen_info> in_scen_info;

    public InputPortBase<String> in_cmd_info;

    private scen_info in_scen_info_value;
    private boolean in_engage_result_value;
    private String in_cmd_info_value;

    public OutputPortBase<Boolean> out_move_finished;
    public OutputPortBase<Boolean> out_fuel_exhasuted;
    public OutputPortBase<move_result> out_move_result;

    private move_result out_move_result_value = new move_result();
    private boolean out_fuel_exhasuted_value = false;
    private boolean out_move_finished_value = false;
    private boolean fuelCheck = false;

    private boolean fuelInterDoneFlag = false;

    private ViewData viewData;

    private Phase IDLE,MOVE,FUEL;

    public ViewData getViewData() {
        return viewData;
    }

    public void setViewData(ViewData viewData) {
        this.viewData = viewData;
    }


    public PlatformManeuverActor(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformManeuverActor(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_engage_result = new InputPortBase<Boolean>(this);
        in_env_info = new InputPortBase<env_info>(this);
        in_scen_info = new InputPortBase<scen_info>(this);

        in_cmd_info = new InputPortBase<String>(this);

        out_move_finished = new OutputPortBase<Boolean>(this);
        out_fuel_exhasuted = new OutputPortBase<Boolean>(this);
        out_move_result = new OutputPortBase<move_result>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new PlatformManeuverActorOm();
        in_scen_info_value = new scen_info();
        in_engage_result_value = true; //默認代表鱼雷尚未命中战舰
        in_cmd_info_value = "0";
    }

    @Override
    protected void constructPhase() {
        IDLE = new Phase("IDLE");   IDLE.setLifeTime(Double.POSITIVE_INFINITY);
        MOVE = new Phase("MOVE");   MOVE.setLifeTime(7.0);
        FUEL = new Phase("FUEL");   FUEL.setLifeTime(0.0);
        this.phase = IDLE;
    }

    @Override
    protected void deltaExternalFunc(Object value) {

        if(this.activePort == in_engage_result){
            /**
             * 得到一個交戰結果，目的是停止机动：
             */
            in_engage_result_value = ((Boolean) value);
            this.phase = IDLE;
            return;
        }

        if(this.phase.getName().equals(IDLE.getName())){
            if(this.activePort == in_scen_info){
                in_scen_info_value = ((scen_info) value);
                String entityName = in_scen_info_value.getResetInfo();
                String[] tt = this.fullName.split("\\.");
                String modelName = tt[2];
                if(entityName.equals("Fleet") && modelName.equals("Fleet")){
                    viewData = new ViewData("Fleet");
                    viewData.origin = viewData.destination = new CartesianPoint(-200, -50, 0);
                    viewData.status = true;

                    try {
                        Visual2dService.getInstance().register(viewData.name, simulator, viewData);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (NamingException e) {
                        e.printStackTrace();
                    }
                    this.phase = MOVE;
                }
                else if(entityName.equals("Submarine") && modelName.equals("Submarine")){
                    viewData= new ViewData("Submarine");
                    viewData.origin = viewData.destination = new CartesianPoint(200, 100, 0);
                    viewData.status = true;

                    try {
                        Visual2dService.getInstance().register(viewData.name, simulator, viewData);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (NamingException e) {
                        e.printStackTrace();
                    }
                    this.phase = MOVE;
                }
                return;
            }
            if(this.activePort == in_cmd_info){
                in_cmd_info_value = ((String) value);
                this.phase = MOVE;
                return;
            }
            return;
        }
        if(this.phase.getName().equals(MOVE.getName())){

            if(this.activePort == in_env_info){
                return;
            }
            if(this.activePort == in_cmd_info){
                in_cmd_info_value = ((String) value);
                return;
            }
            return;
        }
    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase.getName().equals(MOVE.getName())){
            //boolean cmdCheck = (this.viewData.remainingTime > this.phase.getLifeTime());
            //if(cmdCheck){
                this.motion_Equation();
            //}
        }

        if(this.phase.getName().equals(FUEL.getName())){

            fuelCheck = (this.viewData.remainingTime < this.phase.getLifeTime());
            if(fuelCheck){
                out_fuel_exhasuted_value = true;
            }else{
                out_fuel_exhasuted_value = false;
            }
            fuelInterDoneFlag = true;
            return;
        }
    }

    @Override
    protected void lambdaFunc() {
        if(this.phase.getName().equals(MOVE.getName()) && !out_move_result_value.getName().equals("0")){
            this.out_move_result.send(out_move_result_value);
            //out_move_result_value.setName("0");
            this.phase = FUEL;
        }
        if(this.phase.getName().equals(FUEL.getName()) && fuelInterDoneFlag){
             if(fuelCheck){
                 out_fuel_exhasuted.send(true);
                 this.phase = IDLE;
             }else{
                 this.phase = MOVE;
             }
            fuelInterDoneFlag = false;
        }
    }

    private void motion_Equation(){
        this.viewData.origin = this.viewData.destination;

        if (this.viewData.remainingTime < MOVE.getLifeTime()) {
            this.viewData.destination = new CartesianPoint(viewData.origin.x, viewData.origin.y, 0);
            this.viewData.remainingTime = 0.0;
        } else {
            if(in_cmd_info_value.equals("0")){
                viewData.destination = new CartesianPoint(viewData.origin.x+this.viewData.speed, viewData.origin.y+this.viewData.speed, 0);
            }else{
                String[] cmd_info = in_cmd_info_value.split("\\$"); //  "APPROACH$152.2$202.5"
                String cmd = cmd_info[0];
                double x = Double.valueOf(cmd_info[1]);
                double y = Double.valueOf(cmd_info[2]);
                viewData.destination = SimUtil.nextPoint(this.viewData.origin.x,this.viewData.origin.y,x,y,
                        this.viewData.speed,(cmd.equals("APPROACH")));

            }
            if(this.fullName.split("\\.")[2].equals("Submarine")){
                System.out.println("================destination: x = "+viewData.origin.getX()+", y = "+viewData.origin.getY());
                System.out.println("================destination: x = "+viewData.destination.getX()+", y = "+viewData.destination.getY());
                System.out.println();
            }
            this.viewData.remainingTime -= this.phase.getLifeTime();

            this.out_move_result_value.setName(this.viewData.name);
            this.out_move_result_value.setRemainingTime(this.viewData.remainingTime);
            this.out_move_result_value.setPosition( viewData.destination);
            this.out_move_result_value.setCmp(viewData.belong);
            this.out_move_result_value.setDetectRange(viewData.detectRange);
            this.out_move_result_value.setSpeed(viewData.speed);
        }
        this.viewData.startTime = this.simulator.getSimulatorTime();
        this.viewData.stopTime = this.viewData.startTime + this.phase.getLifeTime();
    }
}
