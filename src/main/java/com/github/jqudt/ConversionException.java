package com.github.jqudt;

/**
 * Thrown when the new unit does not have the same parent type
 * @author renaud.richardet@epfl.ch
 *
 */
@SuppressWarnings("serial")
public class ConversionException extends Exception {
	
	public ConversionException(String msg) {super(msg);}
}
