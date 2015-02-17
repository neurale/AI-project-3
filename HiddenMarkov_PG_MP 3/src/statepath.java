package src;

import java.io.FileNotFoundException;

public class statepath
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
		
		lambda.statepath(obsFileName);
	}
}