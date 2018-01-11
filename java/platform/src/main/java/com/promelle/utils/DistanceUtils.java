package com.promelle.utils;

/**
 * This class is intended for providing application wide common functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class DistanceUtils {

	private static final double EARTH_RADIUS = 6371000d;

	/**
	 * This function is responsible for calculating distance between two points
	 * 
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return difference in meters
	 */
	public static double getDistanceFromLatLon(double lat1, double lon1,
			double lat2, double lon2) {
		double latDiff = degreeToRadian(lat2 - lat1);
		double lonDiff = degreeToRadian(lon2 - lon1);
		double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
				+ Math.cos(degreeToRadian(lat1))
				* Math.cos(degreeToRadian(lat2)) * Math.sin(lonDiff / 2)
				* Math.sin(lonDiff / 2);
		return EARTH_RADIUS * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
	}

	/**
	 * This function is responsible for converting degree to radian
	 * 
	 * @param degree
	 * @return radian
	 */
	public static double degreeToRadian(double degree) {
		return degree * (Math.PI / 180);
	}

}
