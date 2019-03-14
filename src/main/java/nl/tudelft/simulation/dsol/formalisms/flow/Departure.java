package nl.tudelft.simulation.dsol.formalisms.flow;

import java.util.Calendar;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The exit station on which statistics are updated and entities destroyed.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author Peter Jacobs, Alexander Verbraeck
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public class Departure<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Station<A, R, T>
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for Departure.
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; on which is scheduled
     */
    public Departure(final DEVSSimulatorInterface<A, R, T> simulator)
    {
        super(simulator);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void receiveObject(final Object object)
    {
        this.fireEvent(StationInterface.RECEIVE_EVENT, object);
        this.fireEvent(StationInterface.RELEASE_EVENT, object);
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Departure.TimeDouble. */
    public static class TimeDouble extends Departure<Double, Double, SimTimeDouble>
            implements StationInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Departure.TimeDouble.
         * @param simulator DEVSSimulatorInterface.TimeDouble; is the simulator on which behavior is scheduled
         */
        public TimeDouble(final DEVSSimulatorInterface.TimeDouble simulator)
        {
            super(simulator);
        }
    }

    /** Easy access class Departure.TimeFloat. */
    public static class TimeFloat extends Departure<Float, Float, SimTimeFloat> implements StationInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Departure.TimeFloat.
         * @param simulator DEVSSimulatorInterface.TimeFloat; is the simulator on which behavior is scheduled
         */
        public TimeFloat(final DEVSSimulatorInterface.TimeFloat simulator)
        {
            super(simulator);
        }
    }

    /** Easy access class Departure.TimeLong. */
    public static class TimeLong extends Departure<Long, Long, SimTimeLong> implements StationInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Departure.TimeLong.
         * @param simulator DEVSSimulatorInterface.TimeLong; is the simulator on which behavior is scheduled
         */
        public TimeLong(final DEVSSimulatorInterface.TimeLong simulator)
        {
            super(simulator);
        }
    }

    /** Easy access class Departure.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Departure<Time, Duration, SimTimeDoubleUnit>
            implements StationInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Departure.TimeDoubleUnit.
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; is the simulator on which behavior is scheduled
         */
        public TimeDoubleUnit(final DEVSSimulatorInterface.TimeDoubleUnit simulator)
        {
            super(simulator);
        }
    }

    /** Easy access class Departure.TimeFloatUnit. */
    public static class TimeFloatUnit extends Departure<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements StationInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Departure.TimeFloatUnit.
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; is the simulator on which behavior is scheduled
         */
        public TimeFloatUnit(final DEVSSimulatorInterface.TimeFloatUnit simulator)
        {
            super(simulator);
        }
    }

    /** Easy access class Departure.CalendarDouble. */
    public static class CalendarDouble extends Departure<Calendar, Duration, SimTimeCalendarDouble>
            implements StationInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Departure.CalendarDouble.
         * @param simulator DEVSSimulatorInterface.CalendarDouble; is the simulator on which behavior is scheduled
         */
        public CalendarDouble(final DEVSSimulatorInterface.CalendarDouble simulator)
        {
            super(simulator);
        }
    }

    /** Easy access class Departure.CalendarFloat. */
    public static class CalendarFloat extends Departure<Calendar, FloatDuration, SimTimeCalendarFloat>
            implements StationInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Departure.CalendarFloat.
         * @param simulator DEVSSimulatorInterface.CalendarFloat; is the simulator on which behavior is scheduled
         */
        public CalendarFloat(final DEVSSimulatorInterface.CalendarFloat simulator)
        {
            super(simulator);
        }
    }

    /** Easy access class Departure.CalendarLong. */
    public static class CalendarLong extends Departure<Calendar, Long, SimTimeCalendarLong>
            implements StationInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Departure.CalendarLong.
         * @param simulator DEVSSimulatorInterface.CalendarLong; is the simulator on which behavior is scheduled
         */
        public CalendarLong(final DEVSSimulatorInterface.CalendarLong simulator)
        {
            super(simulator);
        }
    }

}
