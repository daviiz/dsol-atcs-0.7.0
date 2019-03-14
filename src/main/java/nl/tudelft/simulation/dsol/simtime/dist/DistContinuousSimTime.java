package nl.tudelft.simulation.dsol.simtime.dist;

import java.util.Calendar;

import org.djunits.unit.TimeUnit;
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
import nl.tudelft.simulation.jstats.distributions.Dist;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * Definitions of distributions over SimTime. The distributions wrap a ContinuousDist from the
 * nl.tudelft.simulation.jstats.distributions package in dsol-core.
 * <p>
 * Copyright (c) 2016-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute time type.
 * @param <R> the relative time type.
 * @param <T> the simulation time type.
 */
public abstract class DistContinuousSimTime<A extends Comparable<A>, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends Dist
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** the wrapped distribution. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final DistContinuous wrappedDistribution;

    /**
     * constructs a new continuous distribution.
     * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
     */
    public DistContinuousSimTime(final DistContinuous wrappedDistribution)
    {
        super(wrappedDistribution.getStream());
        this.wrappedDistribution = wrappedDistribution;
    }

    /**
     * draws the next stream value according to the probability of this this distribution.
     * @return the next double value drawn.
     */
    public abstract T draw();

    /**
     * returns the probability density value of an observation.
     * @param observation double; the observation.
     * @return double the probability density.
     */
    public final double probDensity(final double observation)
    {
        return this.wrappedDistribution.probDensity(observation);
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class DistContinuousSimTime.Double. */
    public static class TimeDouble extends DistContinuousSimTime<java.lang.Double, java.lang.Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         */
        public TimeDouble(final DistContinuous wrappedDistribution)
        {
            super(wrappedDistribution);
        }

        /** {@inheritDoc} */
        @Override
        public final SimTimeDouble draw()
        {
            return new SimTimeDouble(super.wrappedDistribution.draw());
        }
    }

    /** Easy access class DistContinuousSimTime.Float. */
    public static class TimeFloat extends DistContinuousSimTime<java.lang.Float, java.lang.Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         */
        public TimeFloat(final DistContinuous wrappedDistribution)
        {
            super(wrappedDistribution);
        }

        /** {@inheritDoc} */
        @Override
        public final SimTimeFloat draw()
        {
            return new SimTimeFloat((float) super.wrappedDistribution.draw());
        }
    }

    /** Easy access class DistContinuousSimTime.Long. */
    public static class TimeLong extends DistContinuousSimTime<java.lang.Long, java.lang.Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         */
        public TimeLong(final DistContinuous wrappedDistribution)
        {
            super(wrappedDistribution);
        }

        /** {@inheritDoc} */
        @Override
        public final SimTimeLong draw()
        {
            return new SimTimeLong((long) super.wrappedDistribution.draw());
        }
    }

    /** Easy access class DistContinuousSimTime.DoubleUnit. */
    public static class TimeDoubleUnit extends DistContinuousSimTime<Time, Duration, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit. */
        private final TimeUnit unit;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         * @param unit TimeUnit; the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public TimeDoubleUnit(final DistContinuous wrappedDistribution, final TimeUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /** {@inheritDoc} */
        @Override
        public final SimTimeDoubleUnit draw()
        {
            return new SimTimeDoubleUnit(new Time(super.wrappedDistribution.draw(), this.unit));
        }
    }

    /** Easy access class DistContinuousSimTime.FloatUnit. */
    public static class TimeFloatUnit extends DistContinuousSimTime<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit. */
        private final TimeUnit unit;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         * @param unit TimeUnit; the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public TimeFloatUnit(final DistContinuous wrappedDistribution, final TimeUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /** {@inheritDoc} */
        @Override
        public final SimTimeFloatUnit draw()
        {
            return new SimTimeFloatUnit(new FloatTime((float) super.wrappedDistribution.draw(), this.unit));
        }
    }

    /** Easy access class DistContinuousSimTime.CalendarDouble. */
    public static class CalendarDouble extends DistContinuousSimTime<Calendar, Duration, SimTimeCalendarDouble>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit. */
        private final TimeUnit unit;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         * @param unit TimeUnit; the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public CalendarDouble(final DistContinuous wrappedDistribution, final TimeUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /** {@inheritDoc} */
        @Override
        public final SimTimeCalendarDouble draw()
        {
            double ms = new Time(super.wrappedDistribution.draw(), this.unit).si * 1000.0;
            return new SimTimeCalendarDouble(ms);
        }
    }

    /** Easy access class DistContinuousSimTime.CalendarFloat. */
    public static class CalendarFloat extends DistContinuousSimTime<Calendar, FloatDuration, SimTimeCalendarFloat>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit. */
        private final TimeUnit unit;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         * @param unit TimeUnit; the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public CalendarFloat(final DistContinuous wrappedDistribution, final TimeUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /** {@inheritDoc} */
        @Override
        public final SimTimeCalendarFloat draw()
        {
            float ms = new FloatTime((float) super.wrappedDistribution.draw(), this.unit).si * 1000.0f;
            return new SimTimeCalendarFloat(ms);
        }
    }

    /** Easy access class DistContinuousSimTime.CalendarLong. */
    public static class CalendarLong extends DistContinuousSimTime<Calendar, Long, SimTimeCalendarLong>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         */
        public CalendarLong(final DistContinuous wrappedDistribution)
        {
            super(wrappedDistribution);
        }

        /** {@inheritDoc} */
        @Override
        public final SimTimeCalendarLong draw()
        {
            long ms = (long) super.wrappedDistribution.draw();
            return new SimTimeCalendarLong(ms);
        }
    }

}
