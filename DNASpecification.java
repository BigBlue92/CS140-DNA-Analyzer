//Ryan mackenzie
//CS140 B: Lab 6 DNA specifications
//Purpose: To read an input file that contains a DNA sequence and determine if the sequence is a protien coding gene.

import java.io.*;
import java.util.*;


public class DNASpecification {

   public static final int CODON_MINIMUM = 5;
   public static final int CG_MASS_MINIMUM = 30;
   public static final int AMOUNT_OF_NUCLEOTIDES = 4;
   public static final int NUCLEOTIDES_PER_CODON = 3;
   
   
   public static void main (String[] args) throws FileNotFoundException{

      Scanner userInput = new Scanner(System.in);
   
      String inputFile = beginProgram(userInput);
      String outputFile = outputFile(userInput);
      int stringAmount = 0;
      String[] fileTxt = new String[100];
      int[] ntNo = new int[AMOUNT_OF_NUCLEOTIDES];
   
      stringAmount = fileReader(inputFile, fileTxt);
          
      outputPrinter(fileTxt, stringAmount, ntNo, outputFile);
      
  
   }//end main
   
   
   
   //Pre: The scanner that interacts with the user.
   //Post: Returns the string of the desired input filename from the user.
   public static String beginProgram(Scanner s) {
        
      System.out.print("This program reports information about DNA\nnucleotide sequences that may encode proteins.\n\nInput file name? ");
      String input = s.next();
      return input;
      
   }//end beginProgram
   
   
   
   //Pre: The scanner that interacts with the user.
   //Post: Returns a string of the user's desired output filename. 
   public static String outputFile(Scanner s) {
   
      System.out.print("Output file name? ");
      String input = s.next();
      return input;
      
   }//end outputFile
   
   
   
   //Pre: The string of the file name, and the array that will hold each string of the file in a seperate element.
   //Post: Builds a string array containing the name of each chain, and then the letter chain in the following element. Returns the total amount of strings stored.
   public static int fileReader(String inputFileName, String[] fileTxt) throws FileNotFoundException {
      Scanner inputFile = new Scanner(new File(inputFileName));
      int stringAmount = 0;
      
      while(inputFile.hasNextLine()) {  
         fileTxt[stringAmount] = inputFile.nextLine();
         stringAmount++;
      }
      
      return stringAmount;
   }//end fileReader
   
   
   
   //Pre: The array which contains the ntide chains, and the index number so the method knows which chain to look at.
   //Post: returns a string with the total letters in each string, ordered ACGT. 
   public static String nCounts(String[] fileTxt, int index, int[] ntNo) {
      Arrays.fill(ntNo, 0);
      String currentChain = fileTxt[index];
      int chainLength = currentChain.length();
      
      for(int index2 = 0; index2 < chainLength; index2++) {
         String letter = currentChain.substring(index2, index2 + 1);
         
         if(letter.equalsIgnoreCase("a")) {
            ntNo[0]++;
         } else if(letter.equalsIgnoreCase("c")) {
            ntNo[1]++;
         } else if(letter.equalsIgnoreCase("g")) {
            ntNo[2]++;
         } else if(letter.equalsIgnoreCase("t")) {
            ntNo[3]++;
         }
      }
      String output = "\nNuc. Counts: " + Arrays.toString(ntNo);
      return output;
   }//end nCounts
   
   
   
   //Pre: The array containing the amount of each letter in the chain, and the array for checking if it's a protien chain.
   //Post: Returns a string with the mass percentages of each letter, and the total mass. Also checks to see if it has the correct percentages to be a protein chain.
   public static String massCalc(int[] ntNo, boolean[] protienCheck) {
      double[] grams = {135.128, 111.103, 151.128, 125.107};
      double[] ntMasses = new double [AMOUNT_OF_NUCLEOTIDES];
      double[] massPercents = new double [AMOUNT_OF_NUCLEOTIDES];
      double totalMasses = 0.0;
      
      for(int add = 0; add < AMOUNT_OF_NUCLEOTIDES; add++) {
         ntMasses[add] = ntNo[add] * grams[add];
         totalMasses = totalMasses + ntMasses[add];
      }
      
      for (int add = 0; add < AMOUNT_OF_NUCLEOTIDES; add++) {
         massPercents[add] = Math.round(((ntMasses[add] / totalMasses) * 100.0) * 10.0) / 10.0;
      }
      
      totalMasses = Math.round(totalMasses * 10.0) / 10.0;
      
      if (massPercents[1] + massPercents[2] > CG_MASS_MINIMUM) {
         protienCheck[1] = true;
      }
      
      String output = "\nTotal Mass %: " + Arrays.toString(massPercents) + " of " + totalMasses;
      return output;
   }
   
   
   //Pre: The array containing all of the strings from the input file, the index of the current chain in the previous array, and an array for the protien check.
   //Post: Returns a string with the chain split into groups of three. ALso checks to see if it has the correct groups to be a protien and changes protienCheck to true.
   public static String codonList(String[] fileTxt, int index, boolean[] protienCheck) {
      String nTides = fileTxt[index];
      int totalCodons = nTides.length() / NUCLEOTIDES_PER_CODON;
      String[] codons = new String [totalCodons];
      int index2 = 0;
      int index3 = 0;
      
      for(int length2 = 0; length2 < totalCodons; length2++) {
         codons[index3] = nTides.substring(index2, index2 + NUCLEOTIDES_PER_CODON);
         codons[index3] = codons[index3].toUpperCase();
         index2 += NUCLEOTIDES_PER_CODON;
         index3++;
      }
      
      if (codons[0].equals("ATG") && totalCodons >= CODON_MINIMUM) {
         if (codons[totalCodons - 1].equals("TAA") || codons[totalCodons - 1].equals("TAG") || codons[totalCodons - 1].equals("TGA")) {
            protienCheck[0] = true;
         }
      }
         
      
      String output = "\nCodons List: " + Arrays.toString(codons);
      return output;
   }
      
    
    
   /*Pre: The array containing all of the sting from the input file, the amount of strings, an array cointaining the number of letters for the current chain,
   and the name of the output file.
   Post: Calculates all of the necessary information about each chain and prints them to the output file one at a time.*/
   public static void outputPrinter(String[] fileTxt, int stringAmount, int[] ntNo, String outputFileName) throws FileNotFoundException {
      int printStop = 0;
      int indexFinder = 0;
      boolean[] protienCheck = new boolean[2];
      PrintStream out = new PrintStream(new File(outputFileName));
      
      while (printStop < stringAmount / 2) {
         
         out.print("Region Name: " + fileTxt[indexFinder] + "\nNucleotides: " + fileTxt[indexFinder + 1] + nCounts(fileTxt, indexFinder + 1, ntNo));
         out.print(massCalc(ntNo, protienCheck) + codonList(fileTxt, indexFinder + 1, protienCheck) + "\n\nIs Protien?: ");
         if(protienCheck[0] && protienCheck[1]) {
            out.println("Yes\n");
         } else {
            out.println("No\n");
         }
         
         Arrays.fill(protienCheck, false);
         indexFinder += 2;
         printStop++;
      }
           
   }//end outputPrinter
   
   
}//end class
   
   