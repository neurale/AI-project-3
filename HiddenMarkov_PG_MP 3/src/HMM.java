package src;

import java.io.*;
import java.util.*;
/*
***************************************************
CS440 Artificial Intelligence
Peter Galvin & Programming project 3
***************************************************
 */

public class HMM 
{
 // Constructor that reads the parameters of a Hidden Markov Model from
 // a .HMM file
 public HMM(String fileName) throws FileNotFoundException
 {
  Scanner s = new Scanner(new File(fileName));
  N = s.nextInt();
  M = s.nextInt();
  T = 5; // T is hard-coded because the given T=4 is not enough
  s.nextLine();
  
  a = new double[N][N];
  b = new double[N][M];
  pi = new double[N];
  states = new String[N];
  observables = new String[M];
  psi = new int[N][T];
  qStar = 0;
  
  for (int i = 0; i < N; i++)
  {
   states[i] = s.next();
  }
  for (int i = 0; i < M; i++)
  {
   observables[i] = s.next();
  }
  s.nextLine();
  s.nextLine();
  
  for (int i = 0; i < N; i++)
  {
   for (int j = 0; j < N; j++)
   {
    a[i][j] = s.nextDouble();
   }
  }
  s.nextLine();
  s.nextLine();
  
  for (int i = 0; i < N; i++)
  {
   for (int j = 0; j < M; j++)
   {
    b[i][j] = s.nextDouble();
   }
  }
  s.nextLine();
  s.nextLine();
  
  for (int i = 0; i < N; i++)
  {
   pi[i] = s.nextDouble();
  }
 }
 
 public HMM(String fileName, int index) throws FileNotFoundException
 {
  Scanner s = new Scanner(new File(fileName));
  
  for (int i = 0; i < 15*(index-1); i++)
  {
      s.nextLine();
  }
  
  N = s.nextInt();
  M = s.nextInt();
  T = 5; // T is hard-coded because the given T=4 is not enough
  s.nextLine();
  
  a = new double[N][N];
  b = new double[N][M];
  pi = new double[N];
  states = new String[N];
  observables = new String[M];
  psi = new int[N][T];
  qStar = 0;
  
  for (int i = 0; i < N; i++)
  {
   states[i] = s.next();
  }
  for (int i = 0; i < M; i++)
  {
   observables[i] = s.next();
  }
  s.nextLine();
  s.nextLine();
  
  for (int i = 0; i < N; i++)
  {
   for (int j = 0; j < N; j++)
   {
    a[i][j] = s.nextDouble();
   }
  }
  s.nextLine();
  s.nextLine();
  
  for (int i = 0; i < N; i++)
  {
   for (int j = 0; j < M; j++)
   {
    b[i][j] = s.nextDouble();
   }
  }
  s.nextLine();
  s.nextLine();
  
  for (int i = 0; i < N; i++)
  {
   pi[i] = s.nextDouble();
  }
 }
 
 
 
 
 /* 
  * The following are used for the class recognize, and occasionally for
  * the class optimize
  */
 public void recognize(String fileName) throws FileNotFoundException,
 IOException
 {
  Scanner s = new Scanner(new File(fileName));
  int numSequences = s.nextInt();
  s.nextLine();

  String[] outputSequence = null;
  
  double prob;
  for (int i = 0; i < numSequences; i++)
  {
   outputSequence = new String[T];
  
   int numObservables = s.nextInt();
   s.nextLine();
   
   for (int j = 0; j < numObservables; j++)
   {
    outputSequence[j] = s.next();
   }
   prob =
     (double)(Math.round(calcProbOfSequence(outputSequence)
     * Math.pow(10, 9)) / Math.pow(10, 9));
   System.out.println(prob);
   
   if (s.hasNextLine()) { s.nextLine(); }
  }
 }
 
 // Given an array of Strings containing an output sequence, returns the
 // probability that the HMM will produce that sequence.
 private double calcProbOfSequence(String[] O) throws IOException
 {
  double probOfSequence = 0;
  
  for (int i = 1; i <= N; i++)
  {
   probOfSequence += alpha(i, T, O);
  }
  
  return probOfSequence;
 }
 
 private double alpha(int J, int t, String[] O) throws IOException
 {
  if (getIndexOfOutputString(O[t-1]) == 0)
  {
   throw new IOException("OBS file has illegal observable");
  }
  if (getIndexOfOutputString(O[t-1]) == -1)
  {
   return alpha(J, t-1, O);
  }
  
  double currentAlpha = 0;
  
  if (t == 1)
  {
   currentAlpha = getPi(J);
   currentAlpha *= getB(J, getIndexOfOutputString(O[0]));
  } else {
   for (int i = 1; i <= N; i++)
   {
    currentAlpha += alpha(i, t-1, O)*getA(i, J);
   }
   currentAlpha *= getB(J, getIndexOfOutputString(O[t-1]));
  }
  
  return currentAlpha;
 }
 
 
 
 
 /*
  * The following are used for the class statepath
  */
 public void statepath(String fileName) throws FileNotFoundException,
 IOException
 {
  Scanner s = new Scanner(new File(fileName));
  int numSequences = s.nextInt();
  s.nextLine();

  String[] outputSequence = null;
  double bestProb;  
  for (int i = 0; i < numSequences; i++)
  {
   outputSequence = new String[T];
  
   int numObservables = s.nextInt();
   s.nextLine();
   
   for (int j = 0; j < numObservables; j++)
   {
    outputSequence[j] = s.next();
   }
   
   bestProb =
     (double)(Math.round(calcMaxProbPathOfSequence(outputSequence)
     * Math.pow(10, 9)) / Math.pow(10, 9));
   System.out.print(bestProb);
   if (bestProb > 0)
   {
    int[] bestStates = new int[numObservables];
    
    // 
    bestStates[numObservables-1] = qStar;
    for (int j = numObservables - 1; j > 0; j--)
    {
     bestStates[j-1] = psi[bestStates[j]-1][j];
    }
    
    for (int j = 0; j < bestStates.length; j++)
    {
     System.out.print(" " + states[bestStates[j]-1]);
    }
   }
   System.out.println();
   
   if (s.hasNextLine()) { s.nextLine(); }
  }
 }
 
 private double calcMaxProbPathOfSequence(String [] O) throws IOException
 {
  double pStar = 0;
  double newProb = 0;
  
  for (int i = 1; i <= N; i++)
  {
   newProb = delta(i, T, O);
   if (newProb > pStar)
   {
    pStar = newProb;
    qStar = i;
   }
  }
  
  return pStar;
 }
 
 // delta simultaneously calculates psi by using the 2D array object psi
 // that is part of the HMM object. It loads the appropriate values into
 // it, then in statepath the desired values are directed into an array
 // called bestStates that is used to create the desired state sequence
 // output.
 private double delta(int J, int t, String[] O) throws IOException
 {
  if (getIndexOfOutputString(O[t-1]) == 0)
  {
   throw new IOException("OBS file has illegal observable");
  }
  // Catches if the T-sized String[] O has elements which are null,
  // as in the case of the output sequence being shorter than the
  // T given by the HMM
  if (getIndexOfOutputString(O[t-1]) == -1)
  {
   return delta(J, t-1, O);
  }
  
  double currentDelta = 0;
  
  if (t == 1)
  {
   currentDelta = getPi(J)*getB(J, getIndexOfOutputString(O[0]));
  } else {
   double newDelta = 0;
   for (int i = 1; i <= N; i++)
   {
    newDelta = delta(i, t-1, O)*getA(i, J);
    if (newDelta > currentDelta)
    {
     currentDelta = newDelta;
     psi[J-1][t-1] = i;
    }
   }
   currentDelta *= getB(J, getIndexOfOutputString(O[t-1]));
  }
  
  return currentDelta;
 }
 
 
 
 
 /*
  * The following are used for the class optimize
  */
 public boolean recognizeOneSequence(String fileName, int index)
   throws FileNotFoundException, IOException
 {
  Scanner s = new Scanner(new File(fileName));
  int numSequences = s.nextInt();
  s.nextLine();

  String[] outputSequence = null;

  // tells the optimize class to not use this method if the given
  // index already exceeds the number of sequences in the OBS file
  if (index > numSequences) {return true;}
  
  double prob;
  for (int i = 0; i < index; i++)
  {
   outputSequence = new String[T];
   
   int numObservables = s.nextInt();
   s.nextLine();
   
   for (int j = 0; j < numObservables; j++)
   {
    outputSequence[j] = s.next();
   }
   
   if (s.hasNextLine()) { s.nextLine(); }
  }
  
  prob =
    (double)(Math.round(calcProbOfSequence(outputSequence)
    * Math.pow(10, 9)) / Math.pow(10, 9));
  System.out.print(prob);

  // tells the optimize class to stop using this method if the sequences
  // in the OBS file have been depleted
  if (index == numSequences) {return true;}
  
  return false;
 }
 //////////////////////////////////////////////////////////////////////////////////////
 public void constructLambdaBar(String fileName, int index, PrintWriter out)
   throws IOException, FileNotFoundException
 {
  double[] piBar = new double[N];
  double[][] aBar = new double[N][N];
  double[][] bBar = new double[N][M];
  
  ////
  Scanner s = new Scanner(new File(fileName));
  int numSequences = s.nextInt();
  s.nextLine();

  String[] O = null;

  // tells the optimize class to not use this method if the given
  // index already exceeds the number of sequences in the OBS file
  if (index > numSequences) {return;}
  
  for (int i = 0; i < index; i++)
  {
   O = new String[T];
   
   int numObservables = s.nextInt();
   s.nextLine();
   
   for (int j = 0; j < numObservables; j++)
   {
    O[j] = s.next();
   }
   
   if (s.hasNextLine()) { s.nextLine(); }
  }
  
  for (int i = 1; i <= N; i++)
  {
   piBar[i-1] = gamma(i,1,O);
   for (int j = 1; j <= N; j++)
   {
    double aBarNumer = 0;
    double aBarDenom = 0;
    for (int m = 1; m <= T-1; m++)
    {
      aBarNumer += ksi(i,j,m,O);
      aBarDenom += gamma(i,m,O);
    }
    if(aBarDenom != 0)
    {
    aBar[i-1][j-1] = aBarNumer/aBarDenom;
    }
    else
    {
     aBar[i-1][j-1]=0;
    }
   }
  }
  for (int j = 1; j <= N; j++)
  {
   for (int k = 1; k <= N; k++)
   {
    double bBarNumer = 0;
    double bBarDenom = 0;
    for (int m = 1; m <= T; m++)
    {
     if (O[m-1] == states[k-1])
     {
      bBarNumer += gamma(j,m,O);
     }
     bBarDenom += gamma(j,m,O);
    }
    bBar[j-1][k-1] = bBarNumer/bBarDenom;
   }
  }
  
  // Write the lambdaBar data just collected into a new file
  out.write(N + " " + M + " " + T + "\r");
  for (int i = 0; i < N; i++)
  {
   out.write(states[i] + " ");
  }
  out.write("\r");
  out.write("kids robots do can play eat chess food");
  out.write("\r");
  out.write("a:" + "\r");
  for(int f = 0; f<N;f++)
  {
   for (int i = 0; i<N;i++)
   {
    out.print(roundDouble(aBar[f][i], 1) + " ");
   }
   out.write("\r");
  }
  out.write("b:"+"\r");
  for(int f = 0; f<N;f++)
  {
   for (int i = 0; i<M;i++)
   {
    out.print(roundDouble(bBar[f][i], 1) + " ");
   }
   out.write("\r");
  }
  out.write("pi:"+"\r");
  for(int i = 0; i<N; i++)
  {
   out.print(roundDouble(piBar[i], 1) + " ");
  }
  out.write("\r");
 }
 ////////////////////////////////////////////////////////////////////////////
 
 public double beta(int I, int t, String[] O) throws IOException
 {
  if (getIndexOfOutputString(O[t-1]) == 0)
  {
   throw new IOException("OBS file has illegal observable");
  }
  // This takes care of the beta(1) case in situations where the
  // given sequence is shorter than T. It does so by checking if
  // the observable AFTER this one is null, in which case it knows
  // this is intended to be the last observable. If the sequence is
  // instead length T, then the (t==T) condition later will catch it.
  if (t < O.length && getIndexOfOutputString(O[t]) == -1)
  {
   return 1;
  }
  
  double currentBeta = 0;
  
  if (t == T)
  {
   currentBeta = 1;
  } else {
   for (int j = 1; j <= N; j++)
   {
    currentBeta += beta(j, t+1, O)*getA(I, j)
      *getB(j, getIndexOfOutputString(O[t]));
   }
  }
  
  return currentBeta;
 }

 private double ksi(int i, int j, int t, String[] O)
   throws IOException
 {
  if (t >= O.length || getIndexOfOutputString(O[t]) <= 0) {return 0;}
  
  return probOfTwoStates(i,j,t,O)/calcProbOfSequence(O);
 }
 
 private double gamma(int i, int t, String[] O)
  throws IOException
 {
  if (t >= O.length || getIndexOfOutputString(O[t]) <= 0) {return 0;}
  
  return alpha(i,t,O)*beta(i,t,O)/calcProbOfSequence(O);
 }

 private double probOfTwoStates(int i, int j, int t, String[] O)
   throws IOException
 {
  return alpha(i,t,O)*getA(i,j)*getB(j,getIndexOfOutputString(O[t]))
    *beta(j,t+1,O);
 }
 
 
 /* 
  * The following are access methods, plus one method
  * getIndexOfOutputString which takes an observable and returns
  * an appropriate index from the HMM object.
  */
 public double getA(int row, int col)
 {
  return a[row-1][col-1];
 }
 
 public double getB(int row, int col)
 {
  return b[row-1][col-1];
 }
 
 public double getPi(int col)
 {
  return pi[col-1];
 }
 
 // Given a string obtained from an OBS file, returns the index of that
 // observable according to the HMM being used. The index can then be
 // used to retrieve the appropriate element from the matrix b. The
 // index can take values from 1 up to M.
 //
 // If the string from the OBS file cannot be matched to an observable
 // in the HMM, the method returns 0. If the string is null, the method
 // returns -1. These cases are addressed appropriately in methods which
 // call getIndexOfOutputString.
 private int getIndexOfOutputString(String output)
 {
  if (output == null)
  {
   return -1;
  }
  
  for (int i = 0; i < M; i++)
  {
   if (output.equals(observables[i]))
   {
    return (i+1);
   }
  }
  
  return 0;
 }
 
 private double roundDouble(double input, int degree)
 {
     double output = (double) Math.round(input * Math.pow(10, degree))
         /Math.pow(10, degree);
     return output;
 }

 private int N;
 private int M;
 private int T;
 private double[][] a;
 private double[][] b;
 private double[] pi;
 private String[] states;
 private String[] observables;
 private int[][] psi;
 private int qStar;
 
}
