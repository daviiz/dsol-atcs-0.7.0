package antiTorpedoCombatSystem.OM;

import antiTorpedoCombatSystem.portType.cmdMsg;
import antiTorpedoCombatSystem.portType.ctrlMsg;
import antiTorpedoCombatSystem.portType.ownPos;
import antiTorpedoCombatSystem.portType.simCtrl;
import asw.soa.data.ViewData;
import asw.soa.util.SimUtil;
import devs.core.ObjectModelBase;
import lombok.Data;
import nl.tudelft.simulation.language.d3.CartesianPoint;

@Data
public class OM_MANEUVER extends ObjectModelBase {
    private double tMove = 10.0;
    private ctrlMsg in_ctrlMsg;
//    private cmdMsg in_cmdMsg;
    private simCtrl in_simCtrl;
    private ownPos out_ownPos;
    /**
     * 视图组件所用数据：
     */
    private ViewData viewData;

    public void CalculationNextPosition(){
        this.viewData.origin = this.viewData.destination;

        if (!this.viewData.status) {
            this.viewData.destination = new CartesianPoint(viewData.destination.x, viewData.destination.y, 0);
        } else {
            if(in_ctrlMsg.getSenderId() == null){
                viewData.destination = new CartesianPoint(viewData.origin.x+this.viewData.speed, viewData.origin.y+this.viewData.speed, 0);
            }else{
                viewData.destination = SimUtil.nextPoint(this.viewData.origin.x,this.viewData.origin.y,this.getIn_ctrlMsg().getTargetPos().x,this.getIn_ctrlMsg().getTargetPos().y,
                        this.viewData.speed,this.getIn_ctrlMsg().getCOMMAND().equals("APPROACH"));
            }

            this.syncData();

            //viewData.destination = new CartesianPoint(viewData.origin.x+viewData.speed, viewData.origin.y+viewData.speed, 0);
            //System.out.println("==============="+this.move_result.location.x);
            //this.out_ownPos.location = viewData.destination;
            //this.out_ownPos.camp = viewData.belong;
        }
    }

    public OM_MANEUVER(){
        in_simCtrl = new simCtrl();
        in_ctrlMsg = new ctrlMsg();
        //in_cmdMsg = new cmdMsg();
        out_ownPos = new ownPos();
        viewData = new ViewData();
        this.syncData();
    }

    /**
     * 同步数据：
     */
    private void syncData(){
        out_ownPos.setPosition(viewData.destination);
        out_ownPos.setCmp(viewData.belong);
        out_ownPos.setName(viewData.name);
        out_ownPos.setLive(viewData.status);
        out_ownPos.setDetectRange(viewData.detectRange);
    }
}
