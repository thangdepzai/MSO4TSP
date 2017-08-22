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
public class Algorithm {

    public static int numberOfFiles; // number of problems
    public static ArrayList<TSP> tsp = new ArrayList<TSP>();
    public static int defaultGeneLength;
    public static Random rand = new Random(5);
    public static int sizePopulation = 250;

    //------------------------------------------------------------------------
    public static void main(String[] args) throws CloneNotSupportedException {
        int max = 0;
        String sCurrentLine = null;
        try (BufferedReader br = new BufferedReader(new FileReader("problems.txt"))) {
            sCurrentLine = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] str = sCurrentLine.split(" "); // split by space
        numberOfFiles = Integer.parseInt(str[0]);

        for (int i = 1; i <= numberOfFiles; i++) {
            int temp = CalculateNumberCity(str[i]); // read data of each tsp
            // problem
            double[][] distances = new double[temp][temp];
            distances = CalculateDistance(temp, str[i], distances);
            TSP t = new TSP();
            t.setDistances(distances);
            t.setLength(temp);
            tsp.add(t);
            if (max < temp) {
                max = temp;
            }
        }
        defaultGeneLength = max;

        System.out.println("Before MFO");
        Population pop = new Population();
        pop.initPopulation();

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        for (int i = 0; i < sizePopulation; i++) {
            if (pop.getPopulation().get(i).getScalarFitness() == 1) {
                printDecodeResult(pop.getPopulation().get(i),
                        tsp.get(pop.getPopulation().get(i).getSkillFactor() - 1));
            }
        }

        for (int i = 0; i < 100000; i++) {
            pop = run(pop);
        }

        System.out.println("After MFO");
        for (int i = 0; i < numberOfFiles; i++) {
            printDecodeResult(pop.getPopulation().get(i), tsp.get(pop.getPopulation().get(i).getSkillFactor() - 1));
        }
    }

    public static Population run(Population pop) throws CloneNotSupportedException {
//        System.out.println("**********************************");
//        for(int k=0;k<pop.getPopulation().size();k++){
//        System.out.println(pop.getPopulation().get(k).getGene());}
//         System.out.println("**********************************");
        Population temp_pop = (Population) pop.clone();
        // individuals
        Population new_pop = new Population(); // population to store 100
        // fittest individuals to next
        // step

        for (int i = 0; i < sizePopulation / 2; i++) {
            // ramdomly choose parents to crossover or mutation
            int parentIndex1, parentIndex2;
            parentIndex1 = rand.nextInt(sizePopulation);
            do {
                parentIndex2 = rand.nextInt(sizePopulation);
            } while (parentIndex1 == parentIndex2);

            if (parentIndex1 >= sizePopulation || parentIndex2 >= sizePopulation) {
                System.out.println("loi index");

            }

            ArrayList<Individual> childs = new ArrayList<Individual>();

            // crossover/mutation
            childs = Population.crossOver(pop.getPopulation().get(parentIndex1), pop.getPopulation().get(parentIndex2));
            // add to temp_pop

            temp_pop.getPopulation().add(childs.get(0));
            temp_pop.getPopulation().add(childs.get(1));
        }

        temp_pop.calculateScalarFitness(temp_pop.getPopulation(), temp_pop.getPopulation().size());
        // calculate scalarFitness and skillFactor of temp_pop
        // sort temp_pop by scalarFitness
        Collections.sort(temp_pop.getPopulation(), new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                if (o1.getScalarFitness() < o2.getScalarFitness()) {
                    return 1;
                } else if (o1.getScalarFitness() > o2.getScalarFitness()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        for (int i = 0; i < sizePopulation; i++) {
            new_pop.getPopulation().add(temp_pop.getPopulation().get(i));
        }
//        temp_pop = null;
//         for(int k=0;k<pop.getPopulation().size();k++){
//        System.out.println(pop.getPopulation().get(k).getGene());}
        pop = null;
        return new_pop;
    }

    public static void printDecodeResult(Individual indiv, TSP tsp) {
        ArrayList<Integer> arr = indiv.decode(tsp);
        for (int i = 0; i < arr.size(); i++) {
            System.out.print(arr.get(i) + " ");
        }
        System.out.println(indiv.fitness.get(indiv.getSkillFactor() - 1) + " " + indiv.getSkillFactor());

    }

    //-------------------------------------------------------------------
    public static int CalculateNumberCity(String fileName) {
        int num = 0; // number of cities
        BufferedReader br = null;
        try {
            String sCurrentLine = null;
            br = new BufferedReader(new FileReader(fileName));
            // read lines 1..4
            for (int j = 0; j < 4; j++) {
                sCurrentLine = br.readLine();
            }
            String[] str = sCurrentLine.split(": "); // split from ": " string
            num = Integer.parseInt(str[1]); // line 4 is containing number
            // of cities.
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return num; // return number of cities
    }

    public static double[][] CalculateDistance(int lengthTSP, String fileName, double[][] distances) {
        BufferedReader br = null;
        try {
            String sCurrentLine = null;
            br = new BufferedReader(new FileReader(fileName));
            // read lines 1..6
            for (int j = 0; j < 6; j++) {
                sCurrentLine = br.readLine();
            }
            City[] cities = new City[lengthTSP];
            // read the coordinates of cities
            for (int j = 0; j < lengthTSP; j++) {
                sCurrentLine = br.readLine();
                String[] str = sCurrentLine.split(" "); // split by space
                cities[j] = new City();
                // set coordinates
                cities[j].setX(Double.parseDouble(str[1]));
                cities[j].setY(Double.parseDouble(str[2]));
                // calculate distances
                for (int i = 0; i <= j; i++) {
                    if (i == j) {
                        distances[j][i] = 0;
                    } else {
                        distances[j][i] = distances[i][j] = Math.sqrt(Math.pow((cities[j].getX() - cities[i].getX()), 2)
                                + Math.pow((cities[j].getY() - cities[i].getY()), 2));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return distances;
    }

}
