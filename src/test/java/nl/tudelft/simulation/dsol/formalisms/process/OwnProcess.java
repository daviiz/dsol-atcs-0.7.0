package nl.tudelft.simulation.dsol.formalisms.process;

import junit.framework.Assert;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * A process used for testing.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class OwnProcess extends Process
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /**
     * @param simulator the simulator
     */
    public OwnProcess(DEVSSimulatorInterface.TimeDouble simulator)
    {
        super(simulator);
    }

    /**
     * processes the process.
     */
    @Override
    public void process()
    {
        try
        {
            SimTime time = super.simulator.getSimTime();
            System.out.println(this + " started @ " + time);
            super.hold(10.0);
            SimTime newTime = super.simulator.getSimTime();
            System.out.println(this + " finished @ " + newTime);
            // TODO Assert.assertTrue((newTime.minus(time)).doubleValue() == 10.0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
