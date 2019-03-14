package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import java.util.HashMap;
import java.util.Map;

import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.exceptions.PortAlreadyDefinedException;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.exceptions.PortNotFoundException;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * AbstractDEVSPortModel class. Adds named ports to the abstract DEVS model.
 * <p>
 * Copyright (c) 2009-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://tudelft.nl/mseck">Mamadou Seck</a><br>
 * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a><br>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public abstract class AbstractDEVSPortModel<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends AbstractDEVSModel<A, R, T>
{
    /** the default serial version UId. */
    private static final long serialVersionUID = 1L;

    /** the map of input port names to input ports. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Map<String, InputPortInterface<A, R, T, ?>> inputPortMap = new HashMap<>();

    /** the map of output port names to output ports. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Map<String, OutputPortInterface<A, R, T, ?>> outputPortMap = new HashMap<>();

    /**
     * Constructor for an abstract DEVS model with ports: we have to indicate the simulator to schedule the events on,
     * and the parent model we are part of. A parent model of null means that we are the top model.
     * @param modelName String; the name of this component
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; the simulator to schedule the events on.
     * @param parentModel CoupledModel&lt;A,R,T&gt;; the parent model we are part of.
     */
    public AbstractDEVSPortModel(final String modelName, final DEVSSimulatorInterface<A, R, T> simulator,
            final CoupledModel<A, R, T> parentModel)
    {
        super(modelName, simulator, parentModel);
    }

    /**
     * Add an input port to the model. Use a name to be able to identify the port later.
     * @param name String; the (unique) name of the input port
     * @param inputPort InputPortInterface&lt;A,R,T,TYPE&gt;; the input port to add
     * @param <TYPE> the type of variable of the input port
     * @throws PortAlreadyDefinedException in case the port name already exist for the model
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected <TYPE> void addInputPort(final String name, final InputPortInterface<A, R, T, TYPE> inputPort)
            throws PortAlreadyDefinedException
    {
        if (this.inputPortMap.containsKey(name))
        {
            throw new PortAlreadyDefinedException(
                    "Adding port " + name + " for model " + this.toString() + ", but it already exists.");
        }
        this.inputPortMap.put(name, inputPort);
    }

    /**
     * Add an output port to the model. Use a name to be able to identify the port later.
     * @param name String; the (unique) name of the output port
     * @param outputPort OutputPortInterface&lt;A,R,T,TYPE&gt;; the output port to add
     * @param <TYPE> the type of variable of the output port
     * @throws PortAlreadyDefinedException in case the port name already exist for the model
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected <TYPE> void addOutputPort(final String name, final OutputPortInterface<A, R, T, TYPE> outputPort)
            throws PortAlreadyDefinedException
    {
        if (this.outputPortMap.containsKey(name))
        {
            throw new PortAlreadyDefinedException(
                    "Adding port " + name + " for model " + this.toString() + ", but it already exists.");
        }
        this.outputPortMap.put(name, outputPort);
    }

    /**
     * Remove an input port from the model. Note: override this method in classes that extend the behavior, e.g. to
     * remove couplings from this port in case it is removed.
     * @param name String; the name of the input port to be removed
     * @throws PortNotFoundException in case the port name does not exist for the model
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected void removeInputPort(final String name) throws PortNotFoundException
    {
        if (!this.inputPortMap.containsKey(name))
        {
            throw new PortNotFoundException(
                    "Removing port " + name + " for model " + this.toString() + ", but it does not exist.");
        }
        this.inputPortMap.remove(name);
    }

    /**
     * Remove an output port from the model. Note: override this method in classes that extend the behavior, e.g. to
     * remove couplings from this port in case it is removed.
     * @param name String; the name of the output port to be removed
     * @throws PortNotFoundException in case the port name does not exist for the model
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected void removeOutputPort(final String name) throws PortNotFoundException
    {
        if (!this.outputPortMap.containsKey(name))
        {
            throw new PortNotFoundException(
                    "Removing port " + name + " for model " + this.toString() + ", but it does not exist.");
        }
        this.outputPortMap.remove(name);
    }

    /**
     * @return inputPortMap; the map of input port names to input ports.
     */
    public final Map<String, InputPortInterface<A, R, T, ?>> getInputPortMap()
    {
        return this.inputPortMap;
    }

    /**
     * @return outputPortMap; the map of output port names to output ports
     */
    public final Map<String, OutputPortInterface<A, R, T, ?>> getOutputPortMap()
    {
        return this.outputPortMap;
    }
}
