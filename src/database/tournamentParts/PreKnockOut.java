package database.tournamentParts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import database.Calculator;

public class PreKnockOut {
	private static String SUFFIX = ".otk";
	private int[] places;
	private int start = 1, end = 2, groups;

	public PreKnockOut(int start, int end, int groups) {
		this.start = start;
		this.end = end;
		this.groups = groups;
		places = new int[groups * (end - start + 1) + 1];
	}

	public int getEnd() {
		return end;
	}

	public int getGroup() {
		return groups;
	}

	public int[] getPlaces() {
		return places;
	}

	public int getStart() {
		return start;
	}

	public void open(String fileName) throws NumberFormatException, IOException {
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String line = null;
		String[] splitted;
		boolean firstLine = true;
		while ((line = in.readLine()) != null) {
			splitted = line.split("#");
			if (firstLine) {
				groups = Integer.parseInt(splitted[0]);
				start = Integer.parseInt(splitted[1]);
				end = Integer.parseInt(splitted[2]);
				places = new int[Calculator.nextPowerOfTwo(groups
						* (end - start + 1) - 1)];
				firstLine = false;
			} else {
				int boxNum = Integer.parseInt(splitted[0]) - 1;
				if (splitted.length < 3) {
					places[boxNum] = 0;
				} else {
					int place = Integer.parseInt(splitted[1]) - 1;
					int group = Integer.parseInt(splitted[2]) - 1;
					places[boxNum] = group * (end - start + 1) + place + 1;
				}
			}
		}
	}

	public void save(String fileName) throws FileNotFoundException, IOException {
		String file;
		if (fileName.endsWith(SUFFIX))
			file = fileName;
		else
			file = fileName + SUFFIX;

		PrintWriter out = new PrintWriter(new FileWriter(file));

		out.println(groups + "#" + start + "#" + end);
		int group, place;
		for (int i = 0; i < places.length; i++) {
			if (places[i] != 0) {
				group = (places[i] - 1) / (end - start + 1) + 1;
				place = (places[i] - 1) % (end - start + 1) + 1;
				out.println((i + 1) + "#" + place + "#" + group);
			} else {
				out.println((i + 1) + "##");
			}
		}

		out.close();
	}

	public void setEnd(int newEnd) {
		end = newEnd;
		places = new int[groups * (end - start + 1) + 1];
	}

	public int setPlace(int idx, int value) {
		int ret = places[idx];
		places[idx] = value;
		return ret;
	}

	public void setStart(int newStart) {
		start = newStart;
		places = new int[groups * (end - start + 1) + 1];
	}
}
