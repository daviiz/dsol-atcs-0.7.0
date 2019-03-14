package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Constant distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/ContinuousDistribution.html">
 * http://mathworld.wolfram.com/ContinuousDistribution.html </a>
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DistConstant extends DistContinuous
{
    /** */
    private static final long serialVersionUID = 1L;

    /** value is the value of the constant distribution. */
    private double value;

    /**
     * constructs a new constant distribution.
     * @param stream StreamInterface; the random number stream
     * @param value double; the value
     */
    public DistConstant(final StreamInterface stream, final double value)
    {
        super(stream);
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        this.stream.nextDouble();
        return this.value;
    }

    /** {@inheritDoc} */
    @Override
    public double probDensity(final double observation)
    {
        if (observation == this.value)
        {
            return 1.0;
        }
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Constant(" + this.value + ")";
    }
}
