package asw_0_8_01.om;

import combatSysModel.portType.move_result;
import combatSysModel.portType.request;
import combatSysModel.portType.response;
import devs.core.ObjectModelBase;
import lombok.Data;

@Data
public class Platform_sensor_updater_om extends ObjectModelBase {

    private move_result in_move_result;
    private request in_request;

    private response out_response;

    public Platform_sensor_updater_om(){
        in_move_result = new move_result();
        in_request = new request();
        out_response = new response();
    }

    /**
     * 更新out_response值
     */
    public void data_integrator(){
        out_response.location = in_move_result.location;
    }



    public move_result getIn_move_result() {
        return in_move_result;
    }

    public void setIn_move_result(move_result in_move_result) {
        this.in_move_result = in_move_result;
    }

    public request getIn_request() {
        return in_request;
    }

    public void setIn_request(request in_request) {
        this.in_request = in_request;
    }

    public response getOut_response() {
        return out_response;
    }

    public void setOut_response(response out_response) {
        this.out_response = out_response;
    }
}
