package antiTorpedoCombatSystem.portType;

import devs.core.PortTypeBase;
import lombok.Data;
import nl.tudelft.simulation.language.d3.CartesianPoint;

/**
 * 发送给平台实体的的原子模型Manever的控制指令：
 */
@Data
public class ctrlMsg extends PortTypeBase {
    private String COMMAND = "APPROACH";  // APPROACH  EVASION
    private CartesianPoint targetPos = new CartesianPoint(Double.NaN,Double.NaN,Double.NaN);


}
