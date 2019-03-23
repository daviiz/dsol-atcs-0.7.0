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
    }

    @Override
    protected void constructPhase() {
        PERIOD = new Phase("PERIOD"); PERIOD.setLifeTime(10.0);
        VA = new Phase("VA"); VA.setLifeTime(0.0);
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
    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase == PERIOD){
            this.phase = VA;
        }
        if(this.phase == VA){
            this.out_engage_result_value = this.DA_Algorithm();

            if(this.out_engage_result_value){
                this.phase = END;
            }else{
                this.phase = PERIOD;
            }

        }
    }

    @Override
    protected void lambdaFunc() {
        if(this.phase == VA){
            if(this.out_engage_result_value){
                //被击毁，交战结果为 false,即拦截失败
                out_engage_result.send(false);
            }
        }
    }

    /**
     * 评判毁伤算法实现：
     * @return
     */
    private boolean DA_Algorithm(){
        /**
         * TO-DO:
         */
        boolean tmp = false;

        if(fleetLoc.getName().equals("0") || torpedoLoc.getName().equals("0") ){

        }else{
            double _distance = SimUtil.calcLength(fleetLoc.getPosition().getX(),fleetLoc.getPosition().getY(),
                    torpedoLoc.getPosition().getX(),torpedoLoc.getPosition().getY());

            if(_distance < 10.0){
                tmp = true;
            }
        }
        return tmp;
    }
}
