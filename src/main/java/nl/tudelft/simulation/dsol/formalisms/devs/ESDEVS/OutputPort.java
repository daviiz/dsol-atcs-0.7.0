package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import java.rmi.RemoteException;

import org.djutils.logger.Cat;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTime;

/**
 * OutputPort class. The output port transfers the event (message) to the next receiver. In case there is no next
 * receiver (e.g. in case of the model being the highest coupled model in the simulation, the event is currently not
 * transferred.
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
 * @param <TYPE> The type of messages the port produces.
 */
public class OutputPort<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>, TYPE>
        implements OutputPortInterface<A, R, T, TYPE>
{
    /** The model to which the port links. */
    private AbstractDEVSModel<A, R, T> model;

    /**
     * Constructor for the output port where the model is a coupled model.
     * @param coupledModel CoupledModel&lt;A,R,T&gt;; the coupled model.
     */
    public OutputPort(final CoupledModel<A, R, T> coupledModel)
    {
        this.model = coupledModel;
    }

    /**
     * Constructor for the output port where the model is an atomic model.
     * @param atomicModel AtomicModel&lt;A,R,T&gt;; the atomic model.
     */
    public OutputPort(final AtomicModel<A, R, T> atomicModel)
    {
        this.model = atomicModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void send(final TYPE value)
    {
        if (this.model.parentModel != null)
        {
            try
            {
                SimLogger.filter(Cat.DSOL).debug("send: TIME IS {}", this.model.getSimulator().getSimulatorTime());
                this.model.parentModel.transfer(this, value);
            }
            catch (RemoteException | SimRuntimeException e)
            {
                SimLogger.always().error(e);
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final AbstractDEVSModel<A, R, T> getModel()
    {
        return this.model;
    }
}
