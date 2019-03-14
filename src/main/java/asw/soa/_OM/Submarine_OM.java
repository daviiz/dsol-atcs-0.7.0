package asw.soa._OM;

import asw.soa._DEM.portType.MoveCmd;
import asw.soa.data.ViewData;
import asw.soa.util.SimUtil;
import nl.tudelft.simulation.language.d3.CartesianPoint;

public class Submarine_OM {

    public static CartesianPoint AlgorithmsOfManeuver(ViewData data, MoveCmd moveCmd){
        CartesianPoint nextFrameDestination ;
        //boolean isFollow = moveCmd.cmd.equals("follow");
        nextFrameDestination = SimUtil.nextPoint(data.origin.x, data.origin.y, moveCmd.threat.x,
                moveCmd.threat.y, data.speed, true);
        return nextFrameDestination;
    }
}
