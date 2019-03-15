package antiTorpedoCombatSystem.OM;

import antiTorpedoCombatSystem.portType.*;
import devs.core.ObjectModelBase;
import lombok.Data;
import nl.tudelft.simulation.language.d3.CartesianPoint;

@Data
public class OM_CONTROL extends ObjectModelBase {
    private double tControl = 15.0;
    private simCtrl in_simCtrl;
    private detect in_detect;
    private ownPos in_ownPos;

    private result out_result;
    private cmdMsg out_cmdMsg;
    private ctrlMsg out_ctrlMsg;
    private simCtrl out_simCtrl;

    private boolean isFired = false;

    public void Identification(){
        //System.out.println("==in_ownPos******"+in_ownPos.getName()+"==in_detect****** "+in_detect.getName());
        if(in_ownPos.getName() == null )
            return;
        if(in_detect.getName() == null)
            return;
        /**
         * 内部机动指令：
         */
        out_ctrlMsg.setTargetPos(in_detect.getPosition());
        //System.out.println("==in_ownPos====="+in_ownPos.getName()+"==in_detect======="+in_detect.getName());

        if(in_ownPos.getName().equals("Fleet")){
            out_ctrlMsg.setCOMMAND("EVASION");

        }else if(in_ownPos.getName().equals("Submarine")){
            out_ctrlMsg.setCOMMAND("APPROACH");

        }
        /**
         * 武器系统指令：
         */
        if(!isFired){
            out_cmdMsg.setCOMMAND("FIRE");
            out_cmdMsg.setTargetPos(in_detect.getPosition());
            out_cmdMsg.setOrginPos(in_ownPos.getPosition());
        }

    }

    public String battlePlanning(){
        String nexrPhaseName = "WAIT";
        if(in_detect.getName() == null){
            return nexrPhaseName;
        }else{
            nexrPhaseName = "IDENTIFY";
        }
//        if(in_ownPos.getName().contains("Fleet")) {
//            ret = "EVASION";
//        }else if(in_ownPos.getName().contains("Submarine")){
//            ret = "APPROACH";
//        }
        return nexrPhaseName;
    }

    public OM_CONTROL(){
        in_simCtrl = new simCtrl();
        in_detect = new detect();
        in_ownPos = new ownPos();

        out_result = new result();
        out_cmdMsg = new cmdMsg();
        out_ctrlMsg = new ctrlMsg();
        out_simCtrl = new simCtrl();
    }
}
