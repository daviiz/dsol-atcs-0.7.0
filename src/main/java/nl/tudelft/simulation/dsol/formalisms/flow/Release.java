package nl.tudelft.simulation.dsol.formalisms.flow;

import java.util.Calendar;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.logger.SimLogger;
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
 * The release station releases a given quantity of a claimed resource.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public class Release<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Station<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20151028L;

    /** resource refers to the resource released. */
    private Resource<A, R, T> resource;

    /** amount defines the amount to be released. */
    private double amount = 1.0;

    /**
     * Constructor for Release.
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; on which is scheduled
     * @param resource Resource&lt;A,R,T&gt;; which is released
     */
    public Release(final DEVSSimulatorInterface<A, R, T> simulator, final Resource<A, R, T> resource)
    {
        this(simulator, resource, 1.0);
    }

    /**
     * Constructor for Release.
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; on which is scheduled
     * @param resource Resource&lt;A,R,T&gt;; which is released
     * @param amount double; of resource which is released
     */
    public Release(final DEVSSimulatorInterface<A, R, T> simulator, final Resource<A, R, T> resource,
            final double amount)
    {
        super(simulator);
        this.resource = resource;
        this.amount = amount;
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized void receiveObject(final Object object)
    {
        super.receiveObject(object);
        try
        {
            this.resource.releaseCapacity(this.amount);
            this.releaseObject(object);
        }
        catch (Exception exception)
        {
            SimLogger.always().warn(exception, "receiveObject");
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Release.TimeDouble. */
    public static class TimeDouble extends Release<Double, Double, SimTimeDouble> implements StationInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator DEVSSimulatorInterface.TimeDouble; on which is scheduled
         * @param resource Resource&lt;Double,Double,SimTimeDouble&gt;; which is released
         */
        public TimeDouble(final DEVSSimulatorInterface.TimeDouble simulator,
                final Resource<Double, Double, SimTimeDouble> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator DEVSSimulatorInterface.TimeDouble; on which is scheduled
         * @param resource Resource&lt;Double,Double,SimTimeDouble&gt;; which is released
         * @param amount double; of resource which is released
         */
        public TimeDouble(final DEVSSimulatorInterface.TimeDouble simulator,
                final Resource<Double, Double, SimTimeDouble> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.TimeFloat. */
    public static class TimeFloat extends Release<Float, Float, SimTimeFloat> implements StationInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator DEVSSimulatorInterface.TimeFloat; on which is scheduled
         * @param resource Resource&lt;Float,Float,SimTimeFloat&gt;; which is released
         */
        public TimeFloat(final DEVSSimulatorInterface.TimeFloat simulator,
                final Resource<Float, Float, SimTimeFloat> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator DEVSSimulatorInterface.TimeFloat; on which is scheduled
         * @param resource Resource&lt;Float,Float,SimTimeFloat&gt;; which is released
         * @param amount double; of resource which is released
         */
        public TimeFloat(final DEVSSimulatorInterface.TimeFloat simulator,
                final Resource<Float, Float, SimTimeFloat> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.TimeLong. */
    public static class TimeLong extends Release<Long, Long, SimTimeLong> implements StationInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator DEVSSimulatorInterface.TimeLong; on which is scheduled
         * @param resource Resource&lt;Long,Long,SimTimeLong&gt;; which is released
         */
        public TimeLong(final DEVSSimulatorInterface.TimeLong simulator,
                final Resource<Long, Long, SimTimeLong> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator DEVSSimulatorInterface.TimeLong; on which is scheduled
         * @param resource Resource&lt;Long,Long,SimTimeLong&gt;; which is released
         * @param amount double; of resource which is released
         */
        public TimeLong(final DEVSSimulatorInterface.TimeLong simulator,
                final Resource<Long, Long, SimTimeLong> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Release<Time, Duration, SimTimeDoubleUnit>
            implements StationInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; on which is scheduled
         * @param resource Resource&lt;Time,Duration,SimTimeDoubleUnit&gt;; which is released
         */
        public TimeDoubleUnit(final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final Resource<Time, Duration, SimTimeDoubleUnit> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; on which is scheduled
         * @param resource Resource&lt;Time,Duration,SimTimeDoubleUnit&gt;; which is released
         * @param amount double; of resource which is released
         */
        public TimeDoubleUnit(final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final Resource<Time, Duration, SimTimeDoubleUnit> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.TimeFloatUnit. */
    public static class TimeFloatUnit extends Release<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements StationInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; on which is scheduled
         * @param resource Resource&lt;FloatTime,FloatDuration,SimTimeFloatUnit&gt;; which is released
         */
        public TimeFloatUnit(final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final Resource<FloatTime, FloatDuration, SimTimeFloatUnit> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; on which is scheduled
         * @param resource Resource&lt;FloatTime,FloatDuration,SimTimeFloatUnit&gt;; which is released
         * @param amount double; of resource which is released
         */
        public TimeFloatUnit(final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final Resource<FloatTime, FloatDuration, SimTimeFloatUnit> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.CalendarDouble. */
    public static class CalendarDouble extends Release<Calendar, Duration, SimTimeCalendarDouble>
            implements StationInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator DEVSSimulatorInterface.CalendarDouble; on which is scheduled
         * @param resource Resource&lt;Calendar,Duration,SimTimeCalendarDouble&gt;; which is released
         */
        public CalendarDouble(final DEVSSimulatorInterface.CalendarDouble simulator,
                final Resource<Calendar, Duration, SimTimeCalendarDouble> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator DEVSSimulatorInterface.CalendarDouble; on which is scheduled
         * @param resource Resource&lt;Calendar,Duration,SimTimeCalendarDouble&gt;; which is released
         * @param amount double; of resource which is released
         */
        public CalendarDouble(final DEVSSimulatorInterface.CalendarDouble simulator,
                final Resource<Calendar, Duration, SimTimeCalendarDouble> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.CalendarFloat. */
    public static class CalendarFloat extends Release<Calendar, FloatDuration, SimTimeCalendarFloat>
            implements StationInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator DEVSSimulatorInterface.CalendarFloat; on which is scheduled
         * @param resource Resource&lt;Calendar,FloatDuration,SimTimeCalendarFloat&gt;; which is released
         */
        public CalendarFloat(final DEVSSimulatorInterface.CalendarFloat simulator,
                final Resource<Calendar, FloatDuration, SimTimeCalendarFloat> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator DEVSSimulatorInterface.CalendarFloat; on which is scheduled
         * @param resource Resource&lt;Calendar,FloatDuration,SimTimeCalendarFloat&gt;; which is released
         * @param amount double; of resource which is released
         */
        public CalendarFloat(final DEVSSimulatorInterface.CalendarFloat simulator,
                final Resource<Calendar, FloatDuration, SimTimeCalendarFloat> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.CalendarLong. */
    public static class CalendarLong extends Release<Calendar, Long, SimTimeCalendarLong>
            implements StationInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator DEVSSimulatorInterface.CalendarLong; on which is scheduled
         * @param resource Resource&lt;Calendar,Long,SimTimeCalendarLong&gt;; which is released
         */
        public CalendarLong(final DEVSSimulatorInterface.CalendarLong simulator,
                final Resource<Calendar, Long, SimTimeCalendarLong> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator DEVSSimulatorInterface.CalendarLong; on which is scheduled
         * @param resource Resource&lt;Calendar,Long,SimTimeCalendarLong&gt;; which is released
         * @param amount double; of resource which is released
         */
        public CalendarLong(final DEVSSimulatorInterface.CalendarLong simulator,
                final Resource<Calendar, Long, SimTimeCalendarLong> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }
}
