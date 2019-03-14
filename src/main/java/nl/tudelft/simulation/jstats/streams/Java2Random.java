package nl.tudelft.simulation.jstats.streams;

import java.util.Random;

import nl.tudelft.simulation.language.DSOLException;
import nl.tudelft.simulation.language.reflection.StateSaver;

/**
 * The Java2Random is an extension of the <code>java.util.Random</code> class which implements the StreamInterface.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Java2Random extends Random implements StreamInterface
{
    /** */
    private static final long serialVersionUID = 20140831L;

    /**
     * seed is a link to the seed value. The reason to store the seed in this variable is that there is no getSeed() on
     * the Java2Random
     */
    private long seed;

    /**
     * creates a new Java2Random and in initializes with System.currentTimeMillis constructs a new Java2Random.
     */
    public Java2Random()
    {
        this(System.currentTimeMillis());
    }

    /**
     * reates a new Java2Random and in initializes with a given seed.
     * @param seed long; the seed to use.
     */
    public Java2Random(final long seed)
    {
        super(seed);
        this.seed = seed;
    }

    /** {@inheritDoc} */
    @Override
    public final void reset()
    {
        this.setSeed(this.seed);
    }

    /** {@inheritDoc} */
    @Override
    public final int nextInt(final int i, final int j)
    {
        return i + (int) Math.floor((j - i + 1) * this.nextDouble());
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized void setSeed(final long seed)
    {
        this.seed = seed;
        super.setSeed(seed);
    }

    /** {@inheritDoc} */
    @Override
    public final long getSeed()
    {
        return this.seed;
    }

    /** {@inheritDoc} */
    @Override
    public final Object saveState() throws StreamException
    {
        try
        {
            return StateSaver.saveState(this);
        }
        catch (DSOLException exception)
        {
            throw new StreamException(exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void restoreState(final Object state) throws StreamException
    {
        try
        {
            StateSaver.restoreState(this, state);
        }
        catch (DSOLException exception)
        {
            throw new StreamException(exception);
        }
    }

}
