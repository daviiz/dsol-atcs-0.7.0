package asw_0_8_01.portMsgType;

import devs.core.PortTypeBase;
import lombok.Data;
import nl.tudelft.simulation.language.d3.CartesianPoint;

@Data
public class threat_info extends PortTypeBase {
    private CartesianPoint position;
    private String name;
    private double remainingTime;
    private int cmp = 0;
    private double detectRange = Double.POSITIVE_INFINITY;
    private double speed = 0;

    public threat_info(){
        this.name = "0";
        remainingTime = Double.POSITIVE_INFINITY;
    }
    public threat_info(move_result value){
        this.name = value.getName();
        this.remainingTime = value.getRemainingTime();
        this.position = value.getPosition();
        this.cmp = value.getCmp();
        this.detectRange = value.getDetectRange();
        this.speed = value.getSpeed();
    }
}
