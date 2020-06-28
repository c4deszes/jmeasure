package org.jmeasure.test.instrument;

/**
 * Used to inject instrument drivers in the JMeasureRunner.
 * <p>
 * Lookup method priority from highest to lowest:
 * <ul>
 * <li>VISA Resource String (<i>recommended</i>)
 * <li>Manufacturer + Serial Number (<i>preferred</i>)
 * <li>Serial Number
 * <li>Manufacturer + Model
 * </ul>
 * 
 * <p>
 * The LXI standard requires that the Manufacturer field is consistent across all their products.
 * 
 * <p>
 * Usage:
 * <pre>
 * //Connects the instrument using the given VISA resource string 
 * {@code @Instrument(resource = "TCPIP0::192.168.1.2::5025::SOCKET")
 * WaveformGenerator wavegen;}
 * 
 * //Connects the instrument using it's device identifier (model number)
 * {@code @Instrument(manufacturer = "Siglent Technologies", qualifier = "SDG1032X")
 * WaveformGenerator wavegen;}
 * 
 * //Connects the instrument using it's device identifier (serial number)
 * {@code @Instrument(manufacturer = "Siglent Technologies", qualifier = "SDG1XABC123456")
 * WaveformGenerator wavegen;}
 * </pre>
 */
public @interface Instrument {

    /**
     * VISA resource string 
     * @return
     */
    String resource() default "";

    /**
     * 
     * @return
     */
    String manufacturer() default "";

    /**
     * Device qualifier, either Model number or Serial Number
     * @return
     */
    String qualifier() default "";
}