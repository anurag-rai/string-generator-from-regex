package org.anurag.stringGenerator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.anurag.stringGenerator.token.StringGenerator;

public class Runner {
	public static void main(String[] args) throws Exception {
		String EXIT = "EXIT";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			try {
				System.out.println("Enter regex:");
				String s = br.readLine();

				if (s.equals(EXIT)) {
					break;
				}
				// Examples:
				// "ab|cd"
				// "((a|b)54([pq]{2,3}))?-[a-e]{,5}"
				// "((a|b)54([pq]{2,3}))?"
				// "[-+]?[0-9]{1,16}[.][0-9]{1,6}"
				// "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{8}", ".{8,12}"
				// "[^aeiouAEIOU0-9]{5}", "[a-f-]{5}"
				// "(1[0-2]|0[1-9])(:[0-5][0-9]){2} (A|P)M"
				// "([abcde]{3}([1-5]{2}))---\2"
				// "(['"])hello\1"

				System.out.println("Enter number of strings to generate:");
				int N = Integer.parseInt(br.readLine());

				RegExpBuilder b = new RegExpBuilder();
				StringGenerator e = b.build(s);
				System.out.println("Generated Strings: ");
				while (N-- > 0) {
					System.out.println(e.generate());
				}
				System.out.println("---------------------------------");
			} catch (Exception e) {
				System.out.println("ERROR: " + e.getMessage());
			}
		}
	}
}
