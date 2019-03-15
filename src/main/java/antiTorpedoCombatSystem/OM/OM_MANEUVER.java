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
    private cmdMsg in_cmdMsg;
    private simCtrl in_simCtrl;
    private ownPos out_ownPos;
    /**
     * 视图组件所用数据：
     */
    private ViewData viewData;

    //武器模型被发射：
    private boolean isFired = false;
    private boolean isWeapon = false;

    public void CalculationNextPosition(){
        if(isFired){
            System.out.println("-------------"+in_cmdMsg.getSenderId());
            this.viewData.origin = this.in_cmdMsg.getOrginPos();
            this.viewData.destination =SimUtil.nextPoint(this.viewData.origin.x,this.viewData.origin.y,this.in_cmdMsg.getTargetPos().x,this.in_cmdMsg.getTargetPos().y,
                    this.viewData.speed,true);
            this.getViewData().isActive = true;
            isFired = false;
            isWeapon = true;
            return;
        }
        if(isWeapon){
            this.viewData.origin = this.viewData.destination;
            if(in_ctrlMsg.getSenderId() == null || in_ctrlMsg.getTargetPos().x == Double.NaN){
                this.viewData.destination =SimUtil.nextPoint(this.viewData.origin.x,this.viewData.origin.y,this.in_cmdMsg.getTargetPos().x,this.in_cmdMsg.getTargetPos().y,
                        this.viewData.speed,true);
            }else{
                viewData.destination = SimUtil.nextPoint(this.viewData.origin.x,this.viewData.origin.y,this.getIn_ctrlMsg().getTargetPos().x,this.getIn_ctrlMsg().getTargetPos().y,
                        this.viewData.speed,this.getIn_ctrlMsg().getCOMMAND().equals("APPROACH"));

            }
        }
        else if(this.getViewData().isActive){
            this.viewData.origin = this.viewData.destination;
            if(in_ctrlMsg.getSenderId() == null || in_ctrlMsg.getTargetPos().x == Double.NaN){
                viewData.destination = new CartesianPoint(viewData.origin.x+this.viewData.speed, viewData.origin.y+this.viewData.speed, 0);
            }else{
                viewData.destination = SimUtil.nextPoint(this.viewData.origin.x,this.viewData.origin.y,this.getIn_ctrlMsg().getTargetPos().x,this.getIn_ctrlMsg().getTargetPos().y,
                        this.viewData.speed,this.getIn_ctrlMsg().getCOMMAND().equals("APPROACH"));

            }
        }

        this.syncData();
    }

    public OM_MANEUVER(){
        in_simCtrl = new simCtrl();
        in_ctrlMsg = new ctrlMsg();
        in_cmdMsg = new cmdMsg();
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
        out_ownPos.setActive(viewData.isActive);
    }
}
