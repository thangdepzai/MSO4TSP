/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author thang
 */
public class Individual {
    

    private ArrayList<Integer> gene = new ArrayList<Integer>();
    private int skillFactor;
    private double scalarFitness;
      ArrayList<Double> fitness = new ArrayList<Double>();
    

    public ArrayList<Integer> getGene() {
        return gene;
    }

    public void setGene(ArrayList<Integer> gene) {
        this.gene = gene;
    }

    public int getSkillFactor() {
        return skillFactor;
    }
    

    public void setSkillFactor(int skillFactor) {
        this.skillFactor = skillFactor;
    }

    public double getScalarFitness() {
        return scalarFitness;
    }

    public void setScalarFitness(double scalarFitness) {
        this.scalarFitness = scalarFitness;
    }
    public void addGene(int value) {
		gene.add(value);
    }
    
    
    //-------------------------------------------------------------------
     public void initGene() {
        for (int i = 0; i < Algorithm.defaultGeneLength; i++) {
            gene.add(i + 1);
        }
        Collections.shuffle(gene,Algorithm.rand);
        setFitness(); // set fitness of this individuals with each problem
    }
    public double CalculaterFitness(TSP tsp, int length) {
        double fitness = 0;
        ArrayList<Integer> decodeResult = decode(tsp);
        for (int i = 0; i < length - 1; i++) {
            fitness = fitness + tsp.getDistances()[decodeResult.get(i) - 1][decodeResult.get(i + 1) - 1];
        }
        fitness += tsp.getDistances()[decodeResult.get(length - 1) - 1][decodeResult.get(0) - 1];
        return fitness;
    }

    public void setFitness() {
        for (int i = 0; i < Algorithm.numberOfFiles; i++) {
            double d = 0;
            d = CalculaterFitness(Algorithm.tsp.get(i), Algorithm.tsp.get(i).getLength());
            fitness.add(d);
        }
    }
    public double getFitness(int j){
        return fitness.get(j);
    }

    
    public ArrayList<Integer> decode(TSP tsp) {
        ArrayList<Integer> decodeResult = new ArrayList<Integer>();
        int num = tsp.getLength();
        for (int i = 0; i < Algorithm.defaultGeneLength; i++) {
           
            if (gene.get(i) <= num) {
                decodeResult.add(gene.get(i));
            }
        }
        return decodeResult;
    }

    
   
    

   
}
