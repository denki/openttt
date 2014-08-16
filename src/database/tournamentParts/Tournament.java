package database.tournamentParts;

import gui.Interaction;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import database.match.Match;
import database.players.Double;
import database.players.Player;
import database.players.Single;
import database.players.Team2;
import exceptions.InputFormatException;

public class Tournament {
	private static String SUFFIX = ".ott";
	private KnockOut knockOut;
	private Properties properties;
	private Qualifying qualifying;
	private Map<Integer, Player> players;
	private int state;

	public Tournament() {
		properties = new Properties();
		qualifying = new Qualifying(1);
		players = new HashMap<Integer, Player>();
		state = 0;
	}

	public static Tournament loadTournament(String fileName) {
		XStream xstream = new XStream(new DomDriver());
		try {
			String xml = Interaction.loadFile(fileName);
			Tournament tournament = (Tournament) xstream.fromXML(xml);
			return tournament;
		} catch (IOException e) {
			System.err
					.println(fileName
							+ " is not of expected XML format. Trying the old binary one.");
			ObjectInputStream objIn;
			try {
				objIn = new ObjectInputStream(new BufferedInputStream(
						new FileInputStream(fileName)));
				Tournament tournament = (database.tournamentParts.Tournament) objIn
						.readObject();
				objIn.close();
				return tournament;
			} catch (FileNotFoundException e1) {
				System.err.println("File " + fileName
						+ " does not exist.");
			} catch (IOException e1) {
				System.err.println("File " + fileName
						+ " is not a OpenTTT file.");
			} catch (ClassNotFoundException e2) {
				System.err.println("File " + fileName
						+ " is not a OpenTTT file.");
			}
		}
		return null;
	}

	public void decState() {
		switch (state) {
		case -1:
			state = 0;
			break;
		case 1:
			state = 0;
			break;
		case 3:
			if (!properties.DO_KNOCKOUT | !properties.DO_QUALIFYING)
				state = 22;
			else
				state = 21;
			break;
		case 4:
			if (properties.DO_QUALIFYING)
				state = 3;
			else
				state = 22;
			break;
		case 6:
			if (properties.DO_KNOCKOUT)
				state = 5;
			else
				state = 3;
			break;
		case 21:
			state = 1;
			break;
		case 22:
			state = 1;
			break;
		default:
			state--;
			setUnsaved(true);
		}
	}

	public boolean get2Team() {
		return properties.TYPE_2TEAM;
	}

	public Tournament getCopy() {
		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(this);
		Tournament tournament = (Tournament) xstream.fromXML(xml);
		return tournament;
	}

	public boolean getDoKnockOut() {
		return properties.DO_KNOCKOUT;
	}

	public boolean getDoQualifying() {
		return properties.DO_QUALIFYING;
	}

	public boolean getDouble() {
		return properties.TYPE_DOUBLE;
	}

	public boolean getDoubleView() {
		return properties.TYPE_DOUBLE | properties.TYPE_2TEAM;
	}

	public int getFinishedGamesCount() {
		int result = 0;
		if (qualifying != null)
			result += qualifying.getMatchByState(2).size();
		if (knockOut != null)
			result += knockOut.getMatchByState(2).size();
		return result;
	}

	public KnockOut getKnockOut() {
		if (knockOut == null)
			knockOut = new KnockOut();
		return knockOut;
	}

	public String getName() {
		return properties.name;
	}

	public Properties getProperties() {
		return properties;
	}

	public Qualifying getQualifying() {
		return qualifying;
	}

	public List<Player> getRanking() {
		List<Player> result = new ArrayList<Player>();
		if (properties.DO_KNOCKOUT)
			return knockOut.getRanking();
		else {
			result.addAll(qualifying.getGroups().get(0).getPlayers());
			Collections.sort(result);
		}
		return result;
	}

	public boolean getSingle() {
		return properties.TYPE_SINGLE;
	}

	public int getState() {
		return state;
	}

	public int getTotalGamesCount() {
		int result = 0;
		if (qualifying != null)
			result += qualifying.getTotalGamesCount();
		if (knockOut != null)
			result += knockOut.getTotalGamesCount();
		return result;
	}

	public void incState() {
		switch (state) {
		case 1:
			if (properties.DO_QUALIFYING & properties.DO_KNOCKOUT)
				state = 21;
			else {
				while (qualifying.getGroups().size() > 1) {
					Group g = qualifying.getGroups().get(
							qualifying.getGroups().size() - 1);
					List<Player> plrs = g.getPlayers();
					for (Player p : plrs)
						qualifying.getUnassigned().add(p);
					qualifying.delGroup(qualifying.getGroups().size() - 1);
				}
				state = 22;
			}
			break;
		case 21:
			state = 3;
			for (Group g : qualifying.getGroups())
				g.startGroup();
			break;
		case 22:
			if (properties.DO_QUALIFYING) {
				state = 3;
				for (Group g : qualifying.getGroups())
					g.startGroup();
			} else
				state = 4;
			break;
		case 3:
			if (properties.DO_KNOCKOUT)
				state = 4;
			else
				state = 6;
			break;
		default:
			state++;
		}
		setUnsaved(true);
	}

	public boolean isUnsaved() {
		if (knockOut == null)
			return qualifying.isUnsaved();
		return qualifying.isUnsaved() | knockOut.isUnsaved();
	}

	public String saveTournament(String fileName) {
		setUnsaved(false);

		// appends ".ott" to filename if not present
		if (!fileName.endsWith(SUFFIX))
			fileName += SUFFIX;

		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(this);
		Interaction.saveText(fileName, xml);
		return fileName;
	}

	public void set2Team(boolean dv) {
		properties.TYPE_2TEAM = dv;
	}

	public void setDoKnockOut(boolean doKnockOut) {
		properties.DO_KNOCKOUT = doKnockOut;
	}

	public void setDoQualifying(boolean doQualifying) {
		properties.DO_QUALIFYING = doQualifying;
	}

	public void setDouble(boolean dv) {
		if (!properties.TYPE_SINGLE & dv) {
			List<Player> help = new ArrayList<Player>();
			for (Group g : qualifying.getGroups()) {
				help.clear();
				for (Player p : g.getPlayers()) {
					Player p1;
					try {
						p1 = new Double(players.size(), p);
						players.put(p.getID(), p);
						help.add(p1);
					} catch (InputFormatException e) {
						System.err
								.println("Can not convert to database.Double");
					} catch (StringIndexOutOfBoundsException e) {
						System.err.println("Not enough data, " + p
								+ " skipped.");
					}
				}
				g.getPlayers().clear();
				g.getPlayers().addAll(help);
			}
			help.clear();
			for (Player p : qualifying.getUnassigned()) {
				Player p1;
				try {
					p1 = new Double(players.size(), p);
					players.put(p.getID(), p);
					help.add(p1);
				} catch (InputFormatException e) {
					System.err
							.println("Can not convert to database.Double");
				} catch (StringIndexOutOfBoundsException e) {
					System.err.println("Not enough data, " + p
							+ " skipped.");
				}
			}
			qualifying.getUnassigned().clear();
			qualifying.getUnassigned().addAll(help);
		}
		properties.TYPE_DOUBLE = dv;
	}

	public void setKnockOut(KnockOut k) {
		setUnsaved(true);
		if (knockOut != null) {
			List<Match> matches = knockOut.getMatches();
			knockOut = k;
			for (Match m : matches)
				k.addMatch(m);
		} else
			knockOut = k;
	}

	public void setName(String name) {
		properties.name = name;
	}

	public void setQualifying(Qualifying q) {
		qualifying = q;
		setUnsaved(true);
	}

	public Single newSingle(String[] splitted) {
		try {
			Single p = new Single(players.size(), this, splitted);
			players.put(p.getID(), p);
			return p;
		} catch (InputFormatException e) {
			return null;
		}
	}
	
	public Double newDouble(String[] splitted) {
		try {
			Double p = new Double(players.size(), this, splitted);
			players.put(p.getID(), p);
			return p;
		} catch (InputFormatException e) {
			return null;
		}
	}
	
	public Team2 newTeam2(String[] splitted) {
		try {
			Team2 p = new Team2(players.size(), this, splitted);
			players.put(p.getID(), p);
			return p;
		} catch (InputFormatException e) {
			return null;
		}
	}

	public Player getPlayer(Integer id) {
		return players.get(id);
	}
	
	public void setSingle(boolean dv) {
		if (!properties.TYPE_SINGLE & dv) {
			for (Group g : qualifying.getGroups()) {
				for (Player p : g.getPlayers()) {
					Player p1 = new Single(players.size(), p);
					players.put(p1.getID(), p1);
					g.getPlayers().set(g.getPlayers().indexOf(p), p1);
				}
			}
			for (Player p : qualifying.getUnassigned()) {
				Player p1 = new Single(players.size(), p);
				players.put(p1.getID(), p1);
				qualifying.getUnassigned().set(
						qualifying.getUnassigned().indexOf(p), p1);
			}
		}
		properties.TYPE_SINGLE = dv;
	}

	private boolean setUnsaved(boolean unsaved) {
		if (unsaved == false) {
			if (knockOut != null)
				knockOut.setUnsaved(false);
			if (qualifying != null)
				qualifying.setUnsaved(false);
		}
		return true;
	}
}
