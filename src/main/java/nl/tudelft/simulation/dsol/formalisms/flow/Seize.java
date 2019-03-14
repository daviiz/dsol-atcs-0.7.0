package nl.tudelft.simulation.dsol.formalisms.flow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.formalisms.ResourceRequestorInterface;
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
import nl.tudelft.simulation.event.EventType;

/**
 * The Seize requests a resource and releases an entity whenever this resource is actually claimed.
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
public class Seize<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Station<A, R, T> implements ResourceRequestorInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140911L;

    /** QUEUE_LENGTH_EVENT is fired when the queue length is changed. */
    public static final EventType QUEUE_LENGTH_EVENT = new EventType("QUEUE_LENGTH_EVENT");

    /** DELAY_TIME is fired when a new delayTime is computed. */
    public static final EventType DELAY_TIME = new EventType("DELAY_TIME");

    /** queue refers to the list of waiting requestors. */
    private List<Request<A, R, T>> queue = Collections.synchronizedList(new ArrayList<Request<A, R, T>>());

    /** requestedCapacity is the amount of resource requested on the resource. */
    private double requestedCapacity = Double.NaN;

    /** resource on which the capacity is requested. */
    private Resource<A, R, T> resource;

    /**
     * Constructor for Seize.
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; on which behavior is scheduled
     * @param resource Resource&lt;A,R,T&gt;; which is claimed
     */
    public Seize(final DEVSSimulatorInterface<A, R, T> simulator, final Resource<A, R, T> resource)
    {
        this(simulator, resource, 1.0);
    }

    /**
     * Constructor for Seize.
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; on which behavior is scheduled
     * @param resource Resource&lt;A,R,T&gt;; which is claimed
     * @param requestedCapacity double; is the amount which is claimed by the seize
     */
    public Seize(final DEVSSimulatorInterface<A, R, T> simulator, final Resource<A, R, T> resource,
            final double requestedCapacity)
    {
        super(simulator);
        if (requestedCapacity < 0.0)
        {
            throw new IllegalArgumentException("requestedCapacity cannot < 0.0");
        }
        this.requestedCapacity = requestedCapacity;
        this.resource = resource;
    }

    /**
     * receives an object which request an amount.
     * @param object Object; the object
     * @param pRequestedCapacity double; the requested capacity
     */
    public final synchronized void receiveObject(final Object object, final double pRequestedCapacity)

    {
        super.receiveObject(object);
        Request<A, R, T> request = new Request<A, R, T>(object, pRequestedCapacity, this.simulator.getSimTime());
        synchronized (this.queue)
        {
            this.queue.add(request);
        }
        try
        {
            this.fireTimedEvent(Seize.QUEUE_LENGTH_EVENT, (double) this.queue.size(),
                    this.simulator.getSimulatorTime());
            this.resource.requestCapacity(pRequestedCapacity, this);
        }
        catch (Exception exception)
        {
            SimLogger.always().warn(exception, "receiveObject");
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void receiveObject(final Object object)
    {
        this.receiveObject(object, this.requestedCapacity);
    }

    /**
     * sets the queue to this seize. This enables seize blocks to share one queue.
     * @param queue List&lt;Request&lt;A,R,T&gt;&gt;; is a new queue.
     */
    public final void setQueue(final List<Request<A, R, T>> queue)
    {
        this.queue = queue;
    }

    /**
     * returns the queue.
     * @return List the queue
     */
    @SuppressWarnings("checkstyle:designforextension")
    public List<Request<A, R, T>> getQueue()
    {
        return this.queue;
    }

    /** {@inheritDoc} */
    @Override
    public final void receiveRequestedResource(final double pRequestedCapacity, final Resource<A, R, T> pResource)

    {
        for (Request<A, R, T> request : this.queue)
        {
            if (request.getAmount() == pRequestedCapacity)
            {
                synchronized (this.queue)
                {
                    this.queue.remove(request);
                }
                this.fireTimedEvent(Seize.QUEUE_LENGTH_EVENT, (double) this.queue.size(),
                        this.simulator.getSimulatorTime());
                R delay = this.simulator.getSimTime().diff(request.getCreationTime());
                this.fireTimedEvent(Seize.DELAY_TIME, delay, this.simulator.getSimulatorTime());
                this.releaseObject(request.getEntity());
                return;
            }
        }
    }

    /**
     * The private RequestClass defines the requests for resource.
     * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
     * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the
     *            absolute and relative types are the same.
     * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
     */
    public static class Request<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
    {
        /** amount is the requested amount. */
        private final double amount;

        /** entity is the object requesting the amount. */
        private final Object entity;

        /** creationTime refers to the moment the request was created. */
        private final T creationTime;

        /**
         * Method Request.
         * @param entity Object; the requesting entity
         * @param amount double; is the requested amount
         * @param creationTime T; the time the request was created
         */
        public Request(final Object entity, final double amount, final T creationTime)
        {
            this.entity = entity;
            this.amount = amount;
            this.creationTime = creationTime;
        }

        /**
         * Returns the amount.
         * @return double
         */
        public final double getAmount()
        {
            return this.amount;
        }

        /**
         * Returns the entity.
         * @return Object
         */
        public final Object getEntity()
        {
            return this.entity;
        }

        /**
         * Returns the creationTime.
         * @return double
         */
        public final T getCreationTime()
        {
            return this.creationTime;
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Seize.TimeDouble. */
    public static class TimeDouble extends Seize<Double, Double, SimTimeDouble> implements StationInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator DEVSSimulatorInterface.TimeDouble; on which behavior is scheduled
         * @param resource Resource&lt;Double,Double,SimTimeDouble&gt;; which is claimed
         */
        public TimeDouble(final DEVSSimulatorInterface.TimeDouble simulator,
                final Resource<Double, Double, SimTimeDouble> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator DEVSSimulatorInterface.TimeDouble; on which behavior is scheduled
         * @param resource Resource&lt;Double,Double,SimTimeDouble&gt;; which is claimed
         * @param requestedCapacity double; is the amount which is claimed by the seize
         */
        public TimeDouble(final DEVSSimulatorInterface.TimeDouble simulator,
                final Resource<Double, Double, SimTimeDouble> resource, final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }
    }

    /** Easy access class Seize.TimeFloat. */
    public static class TimeFloat extends Seize<Float, Float, SimTimeFloat> implements StationInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator DEVSSimulatorInterface.TimeFloat; on which behavior is scheduled
         * @param resource Resource&lt;Float,Float,SimTimeFloat&gt;; which is claimed
         */
        public TimeFloat(final DEVSSimulatorInterface.TimeFloat simulator,
                final Resource<Float, Float, SimTimeFloat> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator DEVSSimulatorInterface.TimeFloat; on which behavior is scheduled
         * @param resource Resource&lt;Float,Float,SimTimeFloat&gt;; which is claimed
         * @param requestedCapacity double; is the amount which is claimed by the seize
         */
        public TimeFloat(final DEVSSimulatorInterface.TimeFloat simulator,
                final Resource<Float, Float, SimTimeFloat> resource, final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }

    }

    /** Easy access class Seize.TimeLong. */
    public static class TimeLong extends Seize<Long, Long, SimTimeLong> implements StationInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator DEVSSimulatorInterface.TimeLong; on which behavior is scheduled
         * @param resource Resource&lt;Long,Long,SimTimeLong&gt;; which is claimed
         */
        public TimeLong(final DEVSSimulatorInterface.TimeLong simulator,
                final Resource<Long, Long, SimTimeLong> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator DEVSSimulatorInterface.TimeLong; on which behavior is scheduled
         * @param resource Resource&lt;Long,Long,SimTimeLong&gt;; which is claimed
         * @param requestedCapacity double; is the amount which is claimed by the seize
         */
        public TimeLong(final DEVSSimulatorInterface.TimeLong simulator,
                final Resource<Long, Long, SimTimeLong> resource, final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }
    }

    /** Easy access class Seize.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Seize<Time, Duration, SimTimeDoubleUnit>
            implements StationInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; on which behavior is scheduled
         * @param resource Resource&lt;Time,Duration,SimTimeDoubleUnit&gt;; which is claimed
         */
        public TimeDoubleUnit(final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final Resource<Time, Duration, SimTimeDoubleUnit> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; on which behavior is scheduled
         * @param resource Resource&lt;Time,Duration,SimTimeDoubleUnit&gt;; which is claimed
         * @param requestedCapacity double; is the amount which is claimed by the seize
         */
        public TimeDoubleUnit(final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final Resource<Time, Duration, SimTimeDoubleUnit> resource, final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }
    }

    /** Easy access class Seize.TimeFloatUnit. */
    public static class TimeFloatUnit extends Seize<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements StationInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; on which behavior is scheduled
         * @param resource Resource&lt;FloatTime,FloatDuration,SimTimeFloatUnit&gt;; which is claimed
         */
        public TimeFloatUnit(final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final Resource<FloatTime, FloatDuration, SimTimeFloatUnit> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; on which behavior is scheduled
         * @param resource Resource&lt;FloatTime,FloatDuration,SimTimeFloatUnit&gt;; which is claimed
         * @param requestedCapacity double; is the amount which is claimed by the seize
         */
        public TimeFloatUnit(final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final Resource<FloatTime, FloatDuration, SimTimeFloatUnit> resource, final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }
    }

    /** Easy access class Seize.CalendarDouble. */
    public static class CalendarDouble extends Seize<Calendar, Duration, SimTimeCalendarDouble>
            implements StationInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator DEVSSimulatorInterface.CalendarDouble; on which behavior is scheduled
         * @param resource Resource&lt;Calendar,Duration,SimTimeCalendarDouble&gt;; which is claimed
         */
        public CalendarDouble(final DEVSSimulatorInterface.CalendarDouble simulator,
                final Resource<Calendar, Duration, SimTimeCalendarDouble> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator DEVSSimulatorInterface.CalendarDouble; on which behavior is scheduled
         * @param resource Resource&lt;Calendar,Duration,SimTimeCalendarDouble&gt;; which is claimed
         * @param requestedCapacity double; is the amount which is claimed by the seize
         */
        public CalendarDouble(final DEVSSimulatorInterface.CalendarDouble simulator,
                final Resource<Calendar, Duration, SimTimeCalendarDouble> resource, final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }
    }

    /** Easy access class Seize.CalendarFloat. */
    public static class CalendarFloat extends Seize<Calendar, FloatDuration, SimTimeCalendarFloat>
            implements StationInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator DEVSSimulatorInterface.CalendarFloat; on which behavior is scheduled
         * @param resource Resource&lt;Calendar,FloatDuration,SimTimeCalendarFloat&gt;; which is claimed
         */
        public CalendarFloat(final DEVSSimulatorInterface.CalendarFloat simulator,
                final Resource<Calendar, FloatDuration, SimTimeCalendarFloat> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator DEVSSimulatorInterface.CalendarFloat; on which behavior is scheduled
         * @param resource Resource&lt;Calendar,FloatDuration,SimTimeCalendarFloat&gt;; which is claimed
         * @param requestedCapacity double; is the amount which is claimed by the seize
         */
        public CalendarFloat(final DEVSSimulatorInterface.CalendarFloat simulator,
                final Resource<Calendar, FloatDuration, SimTimeCalendarFloat> resource, final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }
    }

    /** Easy access class Seize.CalendarLong. */
    public static class CalendarLong extends Seize<Calendar, Long, SimTimeCalendarLong>
            implements StationInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator DEVSSimulatorInterface.CalendarLong; on which behavior is scheduled
         * @param resource Resource&lt;Calendar,Long,SimTimeCalendarLong&gt;; which is claimed
         */
        public CalendarLong(final DEVSSimulatorInterface.CalendarLong simulator,
                final Resource<Calendar, Long, SimTimeCalendarLong> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator DEVSSimulatorInterface.CalendarLong; on which behavior is scheduled
         * @param resource Resource&lt;Calendar,Long,SimTimeCalendarLong&gt;; which is claimed
         * @param requestedCapacity double; is the amount which is claimed by the seize
         */
        public CalendarLong(final DEVSSimulatorInterface.CalendarLong simulator,
                final Resource<Calendar, Long, SimTimeCalendarLong> resource, final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }
    }
}
