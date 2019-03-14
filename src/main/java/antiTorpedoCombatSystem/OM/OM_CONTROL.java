package antiTorpedoCombatSystem.OM;

import antiTorpedoCombatSystem.portType.*;
import devs.core.ObjectModelBase;
import lombok.Data;

@Data
public class OM_CONTROL extends ObjectModelBase {
    private double tControl = 15.0;
    private simCtrl in_simCtrl;
    private detect in_detect;
    private ownPos in_ownPos;

    private result out_result;
    private cmdMsg out_cmdMsg;
    private ctrlMsg out_ctrlMsg;

    public void Identification(){

    }

    public String battlePlanning(){
        return "";
    }

    public OM_CONTROL(){
        in_simCtrl = new simCtrl();
        in_detect = new detect();
        in_ownPos = new ownPos();

        out_result = new result();
        out_cmdMsg = new cmdMsg();
        out_ctrlMsg = new ctrlMsg();
    }
}
