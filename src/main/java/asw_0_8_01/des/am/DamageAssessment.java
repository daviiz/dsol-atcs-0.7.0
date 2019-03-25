package asw_0_8_01.des.am;

import asw.soa.util.SimUtil;
import asw_0_8_01.om.DamageAssessmentOm;
import asw_0_8_01.portMsgType.move_result;
import asw_0_8_01.portMsgType.scen_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class DamageAssessment extends AtomicModelBase<DamageAssessmentOm> {

    public InputPortBase<scen_info> in_scen_info;
    public InputPortBase<move_result> in_move_result;

    public OutputPortBase<Boolean> out_engage_result;

    private boolean out_engage_result_value;

    private move_result in_move_result_value;

    private move_result fleetLoc ;

    private move_result torpedoLoc ;

    /**
     * 毁伤评估是否产生结果：
     * true :表示已经评判出结果
     * false : 表示尚在评判过程中
     */
    private boolean DA_Status;

    private Phase PERIOD,VA,END;

    public DamageAssessment(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public DamageAssessment(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_scen_info = new InputPortBase<scen_info>(this);
        in_move_result = new InputPortBase<move_result>(this);

        out_engage_result = new OutputPortBase<Boolean>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new DamageAssessmentOm();
        in_move_result_value = new move_result();
        out_engage_result_value = false;
        fleetLoc = new move_result();
        torpedoLoc = new move_result();
        DA_Status = false;
    }

    @Override
    protected void constructPhase() {
        PERIOD = new Phase("PERIOD"); PERIOD.setLifeTime(10.0);
        VA = new Phase("VA"); VA.setLifeTime(10);
        END = new Phase("END"); END.setLifeTime(Double.POSITIVE_INFINITY);
        this.phase = PERIOD;
    }

    @Override
    protected void deltaExternalFunc(Object value) {
        if(this.activePort == in_move_result && this.phase == PERIOD){
            in_move_result_value = (move_result)value;
            if(in_move_result_value.getName().equals("Fleet")){
                fleetLoc = in_move_result_value;
            }
            if(in_move_result_value.getName().equals("Torpedo")){
                torpedoLoc = in_move_result_value;
            }

        }
        //毁伤模型重置：
        if(in_scen_info == this.activePort){
            this.phase = PERIOD;
            DA_Status = false;
        }
    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase == PERIOD){
            this.phase = VA;
            return;
        }
        if(this.phase == VA){
            this.DA_Status = this.DA_Algorithm();
        }
    }

    @Override
    protected void lambdaFunc() {
        if(this.phase == VA){
            if(this.DA_Status){
                out_engage_result.send(this.out_engage_result_value);
                this.phase = END;
            }else{
                this.phase = PERIOD;
            }
        }
    }

    /**
     * 评判毁伤算法实现：
     * @return true,成功拦截；false,被鱼雷击中，拦截失败
     */
    private boolean DA_Algorithm(){
        /**
         * TO-DO:
         */
        boolean tmp = false;

        /**
         * 鱼雷燃料耗尽，反鱼雷战斗成功
         */
        if(torpedoLoc.getRemainingTime()<0.000001){
            tmp = true;
            this.out_engage_result_value = true;
        }

        if(fleetLoc.getName().equals("0") || torpedoLoc.getName().equals("0") ){

        }else{
            /**
             * 鱼雷追踪到水面舰艇，反鱼雷战斗失败
             */
            double _distance = SimUtil.calcLength(fleetLoc.getPosition().getX(),fleetLoc.getPosition().getY(),
                    torpedoLoc.getPosition().getX(),torpedoLoc.getPosition().getY());

            if(_distance < 10.0){
                tmp = true;
                this.out_engage_result_value = false;
            }
        }
        return tmp;
    }
}
