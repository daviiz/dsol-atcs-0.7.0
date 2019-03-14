package nl.tudelft.simulation.dsol.simulators;

import java.rmi.RemoteException;

import nl.tudelft.simulation.event.EventType;

/**
 * The AnimatorInterface defines the methods for a DEVSDESS simulator with wallclock delay between the consecutive time
 * steps.
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
public interface AnimatorInterface
{
    /** DEFAULT_ANIMATION_DELAY of 100 milliseconds used in the animator. */
    long DEFAULT_ANIMATION_DELAY = 100L;

    /** UPDATE_ANIMATION_EVENT is fired to wake up animatable components. */
    EventType UPDATE_ANIMATION_EVENT = new EventType("UPDATE_ANIMATION_EVENT");

    /** ANIMATION_DELAY_CHANGED_EVENT is fired when the time step is set. */
    EventType ANIMATION_DELAY_CHANGED_EVENT = new EventType("ANIMATION_DELAY_CHANGED_EVENT");

    /**
     * returns the animation delay between each consequtive animation update.
     * @return the animation delay in milliseconds of wallclock time
     * @throws RemoteException on network failure
     */
    long getAnimationDelay() throws RemoteException;

    /**
     * sets the animationDelay.
     * @param miliseconds long; the animation delay
     * @throws RemoteException on network failure
     */
    void setAnimationDelay(long miliseconds) throws RemoteException;

    /**
     * UpdateAnimation takes care of firing the UPDATE_ANIMATION_EVENT.
     */
    void updateAnimation();

    /**
     * The separate thread that takes care of the animation.
     * <p>
     * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
     * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
     * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which
     * can be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
     * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    class AnimationThread extends Thread
    {
        /** is the animator running? */
        private boolean running = true;

        /** the animator. */
        private final AnimatorInterface animator;

        /**
         * @param animator AnimatorInterface; the animator.
         */
        public AnimationThread(final AnimatorInterface animator)
        {
            super();
            this.animator = animator;
        }

        /** {@inheritDoc} */
        @Override
        public final void run()
        {
            while (this.running)
            {
                try
                {
                    sleep(this.animator.getAnimationDelay());
                    this.animator.updateAnimation();
                }
                catch (InterruptedException exception)
                {
                    // if interrupted by stopAnimation, this.running is false and the animation stops.
                    this.running = false;
                }
                catch (RemoteException exception)
                {
                    this.running = false;
                }
            }
        }

        /**
         * Stop the animation.
         */
        public final void stopAnimation()
        {
            this.running = false;
            interrupt();
        }
    }
}
