package asw.soa.view;

import asw.soa.data.ViewData;
import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.language.d3.DirectedPoint;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.vecmath.Point3d;
import java.rmi.RemoteException;

public class VisualComponent implements Locatable {

    private ViewData _mdata = new ViewData();
    private DEVSSimulatorInterface<Double,Double, SimTimeDouble> simulator = null;

    public VisualComponent(ViewData data, DEVSSimulatorInterface<Double,Double, SimTimeDouble> simulator) {
        this._mdata = data;
        this.simulator = simulator;
    }

    @Override
    public DirectedPoint getLocation() throws RemoteException {
        double fraction = (this.simulator.getSimulatorTime() - this._mdata.startTime) / (this._mdata.stopTime - this._mdata.startTime);
        double x = this._mdata.origin.x + (this._mdata.destination.x - this._mdata.origin.x) * fraction;
        double y = this._mdata.origin.y + (this._mdata.destination.y - this._mdata.origin.y) * fraction;
        return new DirectedPoint(x, y, 0, 0.0, 0.0, this._mdata.theta);
    }

    @Override
    public Bounds getBounds() throws RemoteException {
        return new BoundingSphere(new Point3d(0, 0, 0), ViewData.RADIUS);
    }

}
