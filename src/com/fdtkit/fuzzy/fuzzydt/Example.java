/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fdtkit.fuzzy.fuzzydt;

import com.fdtkit.fuzzy.data.Attribute;
import com.fdtkit.fuzzy.data.Dataset;
import com.fdtkit.fuzzy.data.Row;
import com.fdtkit.fuzzy.utils.AmbiguityMeasure;
import com.fdtkit.fuzzy.utils.FuzzyPartitionEntropyMeasure;
import com.fdtkit.fuzzy.utils.GeneralizedFuzzyPartitionEntropyMeasure;
import com.fdtkit.fuzzy.utils.GeneralizedLeafDeterminer;
import com.fdtkit.fuzzy.utils.LeafDeterminer;
import com.fdtkit.fuzzy.utils.LeafDeterminerBase;
import com.fdtkit.fuzzy.utils.PreferenceMeasure;
import com.fdtkit.fuzzy.utils.QuadraticMappingFunction;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author MHJ
 */
public class Example {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Dataset d = new Dataset();
        
        BufferedReader br = new BufferedReader(new FileReader("data/test.txt"));
        
        String line = "";
        String className = "";
        while((line = br.readLine()) != null) {
            if(line.startsWith("#")) {
                String [] terms = line.substring(line.indexOf(":") + 1).split(" ");
                d.addAttribute(new Attribute(line.substring(1, line.indexOf(":")), terms));
            }
            else if(line.startsWith("@")) {
                String [] terms = line.substring(line.indexOf(":") + 1).split(" ");
                d.addAttribute(new Attribute(line.substring(1, line.indexOf(":") ), terms));                
                className = line.substring(1, line.indexOf(":"));
                d.setClassName(className);
            }
            else if(line.startsWith("=ROW=")) {
                String [] data = line.split(" ");
                Object[] crispRow = new Object[d.getAttributesCount()];
                double[][] fuzzyValues = new double[d.getAttributesCount()][];
                int k = 1;
                for(int i = 0; i < crispRow.length; i++) {
                    crispRow[i] = "Dummy";
                    fuzzyValues[i] = new double[d.getAttribute(i).getLinguisticTermsCount()];
                    for(int j = 0; j < fuzzyValues[i].length; j++) {
                        fuzzyValues[i][j] = Double.parseDouble(data[k++]);
                    }
                }
                
                d.addRow(new Row(crispRow, fuzzyValues));
                
                
            }
        }
        
        br.close();
        
        PreferenceMeasure preferenceMeasure;
        LeafDeterminer leafDeterminer;
        TreeNode root;
        FuzzyDecisionTree descisionTree;
        String[] rules;
        System.out.println("Ambiguity induction fuzzy decision tree");
        preferenceMeasure = new AmbiguityMeasure(0.5);
        leafDeterminer = new LeafDeterminerBase(0.8);        
        descisionTree = new FuzzyDecisionTree(preferenceMeasure, leafDeterminer);
        
        root = descisionTree.buildTree(d);
        
        descisionTree.printTree(root, "");  
        
        rules = descisionTree.generateRules(root);
        for(String rule : rules) {
            System.out.println(rule);
        }
        
        System.out.println("");
  
        System.out.println("Trainingset Prediction:");
        for(int j = 0; j < 16; j++) {
            double[] cVals = descisionTree.classify(j, d, "Plan", rules);
            for(int i = 0; i < cVals.length; i++) {
                System.out.print(String.format("%.2f     ", cVals[i]));
            }
            System.out.println("");
        }         
        
        System.out.println("FID3 fuzzy decision tree");
        preferenceMeasure = new FuzzyPartitionEntropyMeasure();
        leafDeterminer = new LeafDeterminerBase(0.8);        
        descisionTree = new FuzzyDecisionTree(preferenceMeasure, leafDeterminer);
        
        root = descisionTree.buildTree(d);
        
        descisionTree.printTree(root, "");  
        
        rules = descisionTree.generateRules(root);
        for(String rule : rules) {
            System.out.println(rule);
        }
        
        System.out.println("");
  
        System.out.println("Trainingset Prediction:");
        for(int j = 0; j < 16; j++) {
            double[] cVals = descisionTree.classify(j, d, "Plan", rules);
            for(int i = 0; i < cVals.length; i++) {
                System.out.print(String.format("%.2f     ", cVals[i]));
            }
            System.out.println("");
        }         
        System.out.println("GFID3 fuzzy decision tree, with linear maping function {I(t) = t}");
        preferenceMeasure = new GeneralizedFuzzyPartitionEntropyMeasure();
        leafDeterminer = new GeneralizedLeafDeterminer(0.8);
        descisionTree = new FuzzyDecisionTree(preferenceMeasure, leafDeterminer);
        
        
        root = descisionTree.buildTree(d);
        
        descisionTree.printTree(root, "");  
        
        rules = descisionTree.generateRules(root);
        for(String rule : rules) {
            System.out.println(rule);
        }
        
        System.out.println("");
  
        System.out.println("Trainingset Prediction:");
        for(int j = 0; j < 16; j++) {
            double[] cVals = descisionTree.classify(j, d, "Plan", rules);
            for(int i = 0; i < cVals.length; i++) {
                System.out.print(String.format("%.2f     ", cVals[i]));
            }
            System.out.println("");
        } 
        
        System.out.println("GFID3 fuzzy decision tree, with quadratic maping function {I(t) = t^2}");
        preferenceMeasure = new GeneralizedFuzzyPartitionEntropyMeasure(new QuadraticMappingFunction());
        leafDeterminer = new GeneralizedLeafDeterminer(0.8, new QuadraticMappingFunction());
        descisionTree = new FuzzyDecisionTree(preferenceMeasure, leafDeterminer);
        
        
        root = descisionTree.buildTree(d);
        
        descisionTree.printTree(root, "");  
        
        rules = descisionTree.generateRules(root);
        for(String rule : rules) {
            System.out.println(rule);
        }
        
        System.out.println("");
  
        System.out.println("Trainingset Prediction:");
        for(int j = 0; j < 16; j++) {
            double[] cVals = descisionTree.classify(j, d, "Plan", rules);
            for(int i = 0; i < cVals.length; i++) {
                System.out.print(String.format("%.2f     ", cVals[i]));
            }
            System.out.println("");
        }        
    }
    
}
