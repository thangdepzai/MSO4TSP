/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mfo;

/**
 *
 * @author thang
 */

public class TSP {
    // each object of this class will store data of one .tsp file

	private int length; // số thành phố

	private double distances[][]; // matrix n x n lưu khoảng cách giữa các thành phố

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public double[][] getDistances() {
		return distances;
	}

	public void setDistances(double[][] distances) {
		this.distances = distances;
	}


}
