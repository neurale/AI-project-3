package src;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;

public class optimize
{
 public static void main(String[] args) throws java.io.IOException,
 FileNotFoundException
 {
  String hmmFileName = null;
  String obsFileName = null;
  String newHmmFileName = null;
  if (args.length >= 3) {
   hmmFileName = "src/" + args[0];
   obsFileName = "src/" + args[1];
   newHmmFileName = "src/" + args[2];
  } else {
   throw new RuntimeException("Argument(s) missing");
  }
  
  // Construct the HMM-object lambda given the formatted file
  // found at the file name provided by the user
  HMM lambda = new HMM(hmmFileName);
  
  int i = 1;
  boolean done = false;
  while (!done)
  {
   PrintWriter out = new PrintWriter(new FileWriter(newHmmFileName));
   done = lambda.recognizeOneSequence(obsFileName, i);
   lambda.constructLambdaBar(obsFileName, i, out);
   out.close();
   HMM lambdaBar = new HMM(newHmmFileName, i);
   System.out.print(" ");
   lambdaBar.recognizeOneSequence(obsFileName, i);
   System.out.println();
   i++;
  }
  
  
  /*Testing beta:
  System.out.println();
  String[] O = {"kids", "play", "chess", null};
  System.out.print(lambda.beta(2, 1, O));
  */
  
  /* Game plan:
   * 
   * - calculate piBar, aBar, bBar
   * - This should require no more than alpha, beta, a, and b
   * - define ksi as x/(sum(sum(x))), define gamma as sum(ksi)
   *   - x --> probOfStatesSiAndSj (?)
   * - make sure to calc bBar correctly by selecting only Ot = Vk
   * 
   * TO DO:
   *|- define beta
   *|- define probOfTwoStates
   *|- define ksi
   *|- define gamma
   * - calc piBar, aBar, bBar
   *   - These can probably be local variables, not hmm's variables
   * - Put all this in a method called constructLambdaBar
   */
 }
}