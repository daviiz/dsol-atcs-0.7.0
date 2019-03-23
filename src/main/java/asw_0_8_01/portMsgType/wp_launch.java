package asw_0_8_01.portMsgType;

import devs.core.PortTypeBase;
import lombok.Data;

@Data
public class wp_launch extends PortTypeBase {
    private boolean launch = true;
}
