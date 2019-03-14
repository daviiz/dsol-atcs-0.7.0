package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import nl.tudelft.simulation.dsol.simtime.SimTime;

/**
 * EOC class. EOC stands for External Output Coupling, which is a coupling between a component within a coupled model
 * towards the outside of that coupled model. The definition can be found in Zeigler et al. (2000), p. 86-87.
 * <p>
 * Copyright (c) 2009-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://tudelft.nl/mseck">Mamadou Seck</a><br>
 * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a><br>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @param <P> the type of message the EOC produces.
 */
public class EOC<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>, P>
{
    /** the output port of the sending component. */
    private OutputPortInterface<A, R, T, P> fromPort;

    /** the input port of the receiving component. */
    private OutputPortInterface<A, R, T, P> toPort;

    /**
     * Make the wiring between output and input.
     * @param fromPort OutputPortInterface&lt;A,R,T,P&gt;; the output port of the sending component
     * @param toPort OutputPortInterface&lt;A,R,T,P&gt;; input port of the receiving component
     * @throws Exception in case of wiring to self
     */
    public EOC(final OutputPortInterface<A, R, T, P> fromPort, final OutputPortInterface<A, R, T, P> toPort)
            throws Exception
    {
        this.fromPort = fromPort;
        this.toPort = toPort;

        if (this.fromPort.getModel().equals(toPort.getModel()))
        {
            throw new Exception("no self coupling allowed");
        }
    }

    /**
     * @return the output port of the sending component.
     */
    public final OutputPortInterface<A, R, T, P> getFromPort()
    {
        return this.fromPort;
    }

    /**
     * @return the input port of the receiving component.
     */
    public final OutputPortInterface<A, R, T, P> getToPort()
    {
        return this.toPort;
    }
}
