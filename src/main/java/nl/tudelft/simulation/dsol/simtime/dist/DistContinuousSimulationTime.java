package nl.tudelft.simulation.dsol.simtime.dist;

import org.djunits.unit.DurationUnit;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vfloat.scalar.FloatDuration;

import nl.tudelft.simulation.jstats.distributions.Dist;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * Definitions of distributions over relative time. The distributions wrap a ContinuousDist from the
 * nl.tudelft.simulation.jstats.distributions package in dsol-core.
 * <p>
 * Copyright (c) 2016-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <R> the relative time type.
 */
public abstract class DistContinuousSimulationTime<R extends Number & Comparable<R>> extends Dist
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
    public DistContinuousSimulationTime(final DistContinuous wrappedDistribution)
    {
        super(wrappedDistribution.getStream());
        this.wrappedDistribution = wrappedDistribution;
    }

    /**
     * draws the next stream value according to the probability of this this distribution.
     * @return the next double value drawn.
     */
    public abstract R draw();

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

    /** Easy access class DistContinuousTime.Double. */
    public static class TimeDouble extends DistContinuousSimulationTime<java.lang.Double>
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
        public final Double draw()
        {
            return super.wrappedDistribution.draw();
        }
    }

    /** Easy access class DistContinuousTime.Float. */
    public static class TimeFloat extends DistContinuousSimulationTime<java.lang.Float>
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
        public final Float draw()
        {
            return (float) super.wrappedDistribution.draw();
        }
    }

    /** Easy access class DistContinuousTime.Long. */
    public static class TimeLong extends DistContinuousSimulationTime<java.lang.Long>
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
        public final Long draw()
        {
            return (long) super.wrappedDistribution.draw();
        }
    }

    /** Easy access class DistContinuousTime.DoubleUnit. */
    public static class TimeDoubleUnit extends DistContinuousSimulationTime<Duration>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit. */
        private final DurationUnit unit;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         * @param unit DurationUnit; the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public TimeDoubleUnit(final DistContinuous wrappedDistribution, final DurationUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /** {@inheritDoc} */
        @Override
        public final Duration draw()
        {
            return new Duration(super.wrappedDistribution.draw(), this.unit);
        }
    }

    /** Easy access class DistContinuousTime.FloatUnit. */
    public static class TimeFloatUnit extends DistContinuousSimulationTime<FloatDuration>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit. */
        private final DurationUnit unit;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         * @param unit DurationUnit; the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public TimeFloatUnit(final DistContinuous wrappedDistribution, final DurationUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /** {@inheritDoc} */
        @Override
        public final FloatDuration draw()
        {
            return new FloatDuration((float) super.wrappedDistribution.draw(), this.unit);
        }
    }

    /** Easy access class DistContinuousTime.CalendarDouble. */
    public static class CalendarDouble extends DistContinuousSimulationTime<Duration>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit. */
        private final DurationUnit unit;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         * @param unit DurationUnit; the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public CalendarDouble(final DistContinuous wrappedDistribution, final DurationUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /** {@inheritDoc} */
        @Override
        public final Duration draw()
        {
            return new Duration(super.wrappedDistribution.draw(), this.unit);
        }
    }

    /** Easy access class DistContinuousTime.CalendarFloat. */
    public static class CalendarFloat extends DistContinuousSimulationTime<FloatDuration>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit. */
        private final DurationUnit unit;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         * @param unit DurationUnit; the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public CalendarFloat(final DistContinuous wrappedDistribution, final DurationUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /** {@inheritDoc} */
        @Override
        public final FloatDuration draw()
        {
            return new FloatDuration((float) super.wrappedDistribution.draw(), this.unit);
        }
    }

    /** Easy access class DistContinuousTime.CalendarLong. */
    public static class CalendarLong extends DistContinuousSimulationTime<Long>
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
        public final Long draw()
        {
            return (long) super.wrappedDistribution.draw();
        }
    }

}
