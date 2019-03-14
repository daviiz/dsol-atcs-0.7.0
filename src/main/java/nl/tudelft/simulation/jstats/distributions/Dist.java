package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Distribution class forms the basis for all statistical distributions.
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
public abstract class Dist implements java.io.Serializable
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** stream is the random number generator from which to draw. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final StreamInterface stream;

    /**
     * Constructs a new Distribution.
     * @param stream StreamInterface; the stream for this mathematical distribution.
     */
    public Dist(final StreamInterface stream)
    {
        this.stream = stream;
    }

    /**
     * @return stream
     */
    public StreamInterface getStream()
    {
        return this.stream;
    }
}
