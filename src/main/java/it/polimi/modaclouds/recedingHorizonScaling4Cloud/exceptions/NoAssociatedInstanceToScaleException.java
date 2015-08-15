package it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions;

public class NoAssociatedInstanceToScaleException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoAssociatedInstanceToScaleException(String msg) {
		super(msg);	
	}
}
