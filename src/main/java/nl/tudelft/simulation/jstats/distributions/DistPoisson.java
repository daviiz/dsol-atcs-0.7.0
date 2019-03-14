package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.math.ProbMath;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Poisson distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/PoissonDistribution.html">
 * http://mathworld.wolfram.com/PoissonDistribution.html </a>
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
public class DistPoisson extends DistDiscrete
{
    /** */
    private static final long serialVersionUID = 1L;

    /** lambda is the lambda parameter. */
    private double lambda;

    /** expl is a helper variable. */
    private double expl;

    /**
     * constructs a new Poisson distribution.
     * @param stream StreamInterface; the random number stream
     * @param lambda double; the lambda parameter
     */
    public DistPoisson(final StreamInterface stream, final double lambda)
    {
        super(stream);
        if (lambda > 0.0)
        {
            this.lambda = lambda;
        }
        else
        {
            throw new IllegalArgumentException("Error Poisson - lambda<=0");
        }
        this.expl = Math.exp(-this.lambda);
    }

    /** {@inheritDoc} */
    @Override
    public long draw()
    {
        // Adapted from Fortran program in Shannon, Systems Simulation, 1975,
        // p. 359
        double s = 1.0;
        long x = -1;
        do
        {
            s = s * this.stream.nextDouble();
            x++;
        }
        while (s > this.expl);
        return x;
    }

    /** {@inheritDoc} */
    @Override
    public double probability(final int observation)
    {
        if (observation >= 0)
        {
            return (Math.exp(-this.lambda) * Math.pow(this.lambda, observation)) / ProbMath.faculty(observation);
        }
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Poisson(" + this.lambda + ")";
    }
}
