/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author thang
 */
public class Population implements Cloneable {

    private ArrayList<Individual> population = new ArrayList<Individual>(Algorithm.sizePopulation);

    public ArrayList<Individual> getPopulation() {
        return population;
    }

    public Object clone() throws CloneNotSupportedException {
        Population clonePopulation = (Population) super.clone();
        clonePopulation.population = (ArrayList<Individual>) clonePopulation.population.clone();
        return clonePopulation;
    }

    public void initPopulation() {
        for (int i = 0; i < Algorithm.sizePopulation; i++) {
            Individual indiv = new Individual();
            indiv.initGene();
            if (!population.contains(indiv)) {
                population.add(indiv);

            } else {
                i--;
            }
        }
        calculateScalarFitness(population, Algorithm.sizePopulation);
        //      System.out.println("#########################################################################\n\n\n");
    }

    public static ArrayList<Individual> crossOver(Individual indiv1, Individual indiv2) {
        ArrayList<Individual> child = new ArrayList<Individual>();
        if (indiv1.getSkillFactor() == indiv2.getSkillFactor()) {
         //   System.out.println("lai ghep");
            child = pMX(indiv1, indiv2);
            child.get(0).setFitness();
            child.get(1).setFitness();
         //   System.out.println("het lai ghep");
            return child;
        } else {
            child.add(mutation(indiv1));
            child.add(mutation(indiv2));
            return child;
        }
    }

    public static Individual mutation(Individual indiv) {
        Individual ind = new Individual();
        int left, right; // mutation point
        left = Algorithm.rand.nextInt(Algorithm.defaultGeneLength);
        do {
            right = Algorithm.rand.nextInt(Algorithm.defaultGeneLength);
        } while (right == left);
        for (int i = 0; i < Algorithm.defaultGeneLength; i++) {
            if (i == left) {
                ind.addGene(indiv.getGene().get(right));
            } else if (i == right) {
                ind.addGene(indiv.getGene().get(left));
            } else {
                ind.addGene(indiv.getGene().get(i));
            }
        }
        ind.setFitness();
        return ind;
    }

    public void calculateScalarFitness(ArrayList<Individual> pop, int popLength) {
        double[] scalarFitness = new double[popLength];
        int[] skillFactor = new int[popLength];

        ArrayList<Individual> temp_pop = (ArrayList<Individual>) pop.clone();

        for (int i = 0; i < popLength; i++) {
            scalarFitness[i] = 0;
            temp_pop.get(i).setScalarFitness(0);

        }

        for (int i = 0; i < Algorithm.numberOfFiles; i++) {
            int j = i;
            Collections.sort(temp_pop, new Comparator<Individual>() {
                @Override

                public int compare(Individual o1, Individual o2) {
                    if (o1.getFitness(j) > o2.getFitness(j)) {
                        return 1;
                    } else if (o1.getFitness(j) < o2.getFitness(j)){
                        return -1;
                    }
                    else return 0;

                }
            });

            for (int k = 0; k < popLength; k++) {
                Individual ind = pop.get(k);
                if (1.0 / (temp_pop.indexOf(ind) + 1) > scalarFitness[k]) {
                    scalarFitness[k] = (1.0 / (temp_pop.indexOf(ind) + 1));
                    skillFactor[k] = i + 1;
                }
            }
        }
        for (int k = 0; k < popLength; k++) {
            pop.get(k).setScalarFitness(scalarFitness[k]);
            pop.get(k).setSkillFactor(skillFactor[k]);
        }
        skillFactor = null;
        scalarFitness = null;

    }

    public static ArrayList<Individual> pMX(Individual indiv1, Individual indiv2) {

        ArrayList<Individual> child = new ArrayList<Individual>();
        Individual ind1 = new Individual();
        Individual ind2 = new Individual();
        int left, right; // cross over point
        left = Algorithm.rand.nextInt(Algorithm.defaultGeneLength);
        do {
            right = Algorithm.rand.nextInt(Algorithm.defaultGeneLength);
        } while (right == left); // left, right must be two different number
        if (left > right) {
            int temp = right;
            right = left;
            left = temp;
        } // left always must be smaller than right
        // generate child1
        ArrayList<Integer> cutoff1 = new ArrayList<Integer>();
        ArrayList<Integer> cutoff2 = new ArrayList<Integer>();
        for (int i = left; i <= right; i++) {
            cutoff1.add(indiv1.getGene().get(i));
            cutoff2.add(indiv2.getGene().get(i));

        }

        for (int i = 0; i < Algorithm.defaultGeneLength; i++) {
            if (!cutoff1.contains(indiv2.getGene().get(i))) {
                ind1.addGene(indiv2.getGene().get(i));
            }
            if (!cutoff2.contains(indiv1.getGene().get(i))) {
                ind2.addGene(indiv1.getGene().get(i));

            }
        }
        ind1.getGene().addAll(left, cutoff1);
        child.add(ind1);
        ind2.getGene().addAll(left, cutoff2);
        child.add(ind2);
//        System.out.println(indiv2.getGene());
//        System.out.println(indiv1.getGene());
//        System.out.println(cutoff2);
//        System.out.println(ind2.getGene());
//        System.out.println(ind2.getGene().size());
        return child;
    }

}
