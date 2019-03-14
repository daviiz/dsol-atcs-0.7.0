package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Exponential distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/ExponentialDistribution.html">
 * http://mathworld.wolfram.com/ExponentialDistribution.html </a>
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
public class DistExponential extends DistContinuous
{
    /** */
    private static final long serialVersionUID = 1L;

    /** mean is the mean value of the exponential distribution. */
    private double mean;

    /**
     * constructs a new exponential function. The exponential distribution describes the interarrival times of
     * "cutomers" to a system that occur at a constant rate.
     * @param stream StreamInterface; the random number stream
     * @param mean double; the mean (mean &gt; 0)
     */
    public DistExponential(final StreamInterface stream, final double mean)
    {
        super(stream);
        if (mean > 0.0)
        {
            this.mean = mean;
        }
        else
        {
            throw new IllegalArgumentException("Error Exponential - mean<=0");
        }
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        return -this.mean * Math.log(this.stream.nextDouble());
    }

    /** {@inheritDoc} */
    @Override
    public double probDensity(final double observation)
    {
        if (observation >= 0)
        {
            return (1 / this.mean) * Math.exp(-observation / this.mean);
        }
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Exponential(" + this.mean + ")";
    }
}
