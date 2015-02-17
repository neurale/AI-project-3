package src;

import java.io.FileNotFoundException;

// Prints the probability that each output sequence will be produced by
// the given HMM, given an OBS file with any number of observation
// sequences.
public class recognize
{
	public static void main(String[] args) throws java.io.IOException,
	FileNotFoundException
	{
		String hmmFileName = null;
		String obsFileName = null;
		if (args.length >= 2) {
			hmmFileName = "src/" + args[0];
			obsFileName = "src/" + args[1];
		} else {
			throw new RuntimeException("Argument(s) missing");
		}
		
		// Construct the HMM-object lambda given the formatted file
		// found at the file name provided by the user
		HMM lambda = new HMM(hmmFileName);
		
		lambda.recognize(obsFileName);
	}
}
