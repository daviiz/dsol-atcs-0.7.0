package nl.tudelft.simulation.dsol.simtime;

import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

/**
 * The SimTime class with a FloatTime as the absolute time, and a FloatDuration as the relative time. The units are
 * defined in the djunits package. More information can be found at
 * <a href="https://djunits.org">https://djunits.org</a>.
 * <p>
 * Copyright (c) 2016-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class SimTimeFloatUnit extends SimTime<FloatTime, FloatDuration, SimTimeFloatUnit>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** time as a DJUNITS object. */
    private FloatTime time;

    /**
     * @param time FloatTime; the initial time.
     */
    public SimTimeFloatUnit(final FloatTime time)
    {
        super(time);
    }

    /** {@inheritDoc} */
    @Override
    public final void add(final FloatDuration relativeTime)
    {
        this.time = this.time.plus(relativeTime);
    }

    /** {@inheritDoc} */
    @Override
    public final void subtract(final FloatDuration relativeTime)
    {
        this.time = this.time.minus(relativeTime);
    }

    /** {@inheritDoc} */
    @Override
    public FloatDuration diff(final FloatTime simTime)
    {
        return this.time.minus(simTime);
    }

    /** {@inheritDoc} */
    @Override
    public final SimTimeFloatUnit setZero()
    {
        this.time = FloatTime.ZERO;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public final SimTimeFloatUnit copy()
    {
        return new SimTimeFloatUnit(this.time);
    }

    /** {@inheritDoc} */
    @Override
    public final void set(final FloatTime absoluteTime)
    {
        this.time = absoluteTime;
    }

    /** {@inheritDoc} */
    @Override
    public final FloatTime get()
    {
        return this.time;
    }

    /** {@inheritDoc} */
    @Override
    public FloatTime getAbsoluteZero()
    {
        return FloatTime.ZERO;
    }

    /** {@inheritDoc} */
    @Override
    public FloatDuration getRelativeZero()
    {
        return FloatDuration.ZERO;
    }

}
