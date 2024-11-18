//package com.example.cm2601_cw_javafx;
//
//import org.apache.opennlp.tools.doccat.DoccatModel;
//import org.apache.opennlp.tools.doccat.DocumentCategorizerME;
//import opennlp.tools.doccat.DocumentCategorizerME;
//
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//public class NLPCategorizer {
//    private DoccatModel model;
//
//    // Constructor that loads the trained model
//    public NLPCategorizer(String modelPath) {
//        try (InputStream modelIn = new FileInputStream(modelPath)) {
//            model = new DoccatModel(modelIn);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Method to categorize a given article
//    public String categorize(String article) {
//        DocumentCategorizerME categorizer = new DocumentCategorizerME(model);
//        double[] outcomes = categorizer.categorize(article);
//        return categorizer.getBestCategory(outcomes);
//    }
//
//    public static void main(String[] args) {
//        // Load the model from the file path where you saved en-doccat.bin
//        NLPCategorizer categorizer = new NLPCategorizer("en-doccat.bin");
//
//        // Example usage: categorizing a sample article
//        String article = "Advances in AI are leading to significant breakthroughs in various fields.";
//        String category = categorizer.categorize(article);
//
//        System.out.println("Predicted Category: " + category);
//    }
//}
