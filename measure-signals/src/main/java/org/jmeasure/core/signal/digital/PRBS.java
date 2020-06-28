package org.jmeasure.core.signal.digital;

import java.util.Random;

import org.jmeasure.core.signal.Signal;

/**
 * PRBS
 */
public class PRBS extends Signal<Boolean> {

    private final static Random random = new Random();

    public PRBS(int bitCount, float bitRate) {
        super("PRBS_" + bitRate + "bps_" + bitCount);

        //random.nextBytes(bytes);
    }

}