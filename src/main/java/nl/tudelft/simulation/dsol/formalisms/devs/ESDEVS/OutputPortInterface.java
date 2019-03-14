package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import java.rmi.RemoteException;
import java.util.Calendar;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;

/**
 * OutputPortInterface class. Describes the contract for an output port of the Classic Parallel DEVS Atomic Model with
 * Ports conform Zeigler et al (2000), section 4.2.2. and section 4.3 (pp. 84 ff).
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
 * @param <TYPE> The type of messages the input port accepts.
 */
public interface OutputPortInterface<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>, TYPE>
{

    /**
     * Send a message through the output port.
     * @param value TYPE; the value to transfer.
     * @throws SimRuntimeException a simulation runtime exception
     * @throws RemoteException a remote exception
     */
    void send(TYPE value) throws SimRuntimeException, RemoteException;

    /**
     * @return the model to which the port belongs.
     */
    AbstractDEVSModel<A, R, T> getModel();

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /**
     * Easy access interface OutputPortInterface.Double.
     * @param <P> The type of message the input port accepts
     */
    public interface TimeDouble<P> extends OutputPortInterface<Double, Double, SimTimeDouble, P>
    {
        // typed extension
    }

    /**
     * Easy access interface OutputPortInterface.Float.
     * @param <P> The type of message the input port accepts
     */

    public interface TimeFloat<P> extends OutputPortInterface<Float, Float, SimTimeFloat, P>
    {
        // typed extension
    }

    /**
     * Easy access interface OutputPortInterface.Long.
     * @param <P> The type of message the input port accepts
     */
    public interface TimeLong<P> extends OutputPortInterface<Long, Long, SimTimeLong, P>
    {
        // typed extension
    }

    /**
     * Easy access interface OutputPortInterface.DoubleUnit.
     * @param <P> The type of message the input port accepts
     */
    public interface TimeDoubleUnit<P> extends OutputPortInterface<Time, Duration, SimTimeDoubleUnit, P>
    {
        // typed extension
    }

    /**
     * Easy access interface OutputPortInterface.FloatUnit.
     * @param <P> The type of message the input port accepts
     */
    public interface TimeFloatUnit<P> extends OutputPortInterface<FloatTime, FloatDuration, SimTimeFloatUnit, P>
    {
        // typed extension
    }

    /**
     * Easy access interface OutputPortInterface.CalendarDouble.
     * @param <P> The type of message the input port accepts
     */
    public interface CalendarDouble<P> extends OutputPortInterface<Calendar, Duration, SimTimeCalendarDouble, P>
    {
        // typed extension
    }

    /**
     * Easy access interface OutputPortInterface.CalendarFloat.
     * @param <P> The type of message the input port accepts
     */
    public interface CalendarFloat<P> extends OutputPortInterface<Calendar, FloatDuration, SimTimeCalendarFloat, P>
    {
        // typed extension
    }

    /**
     * Easy access interface OutputPortInterface.CalendarLong.
     * @param <P> The type of message the input port accepts
     */
    public interface CalendarLong<P> extends OutputPortInterface<Calendar, Long, SimTimeCalendarLong, P>
    {
        // typed extension
    }

}
