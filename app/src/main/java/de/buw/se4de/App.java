/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package de.buw.se4de;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class App {

	public static void main(String[] args) {
		grid first_grid = new grid();
		//first_grid.cells[0][0].dead = true;

		Scanner scanner = new Scanner(System.in);
		System.out.println("Do you want to play a game? y/n" );
		String input = scanner.nextLine();
		if (Objects.equals(input, "y")){
			first_grid.playSingleGame();
		}
	}
}
