package antiTorpedoCombatSystem.portType;

import devs.core.PortTypeBase;
import lombok.Data;
import nl.tudelft.simulation.language.d3.CartesianPoint;

/**
 * 从潜艇输出给鱼雷、或者从战舰输出给鱼雷诱饵模型的指令：
 */
@Data
public class cmdMsg extends PortTypeBase {

    private String COMMAND = "FIRE";
    private CartesianPoint orginPos= new CartesianPoint(Double.NaN,Double.NaN,Double.NaN);
    private CartesianPoint targetPos= new CartesianPoint(Double.NaN,Double.NaN,Double.NaN);

}
