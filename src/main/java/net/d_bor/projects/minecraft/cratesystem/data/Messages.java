package net.d_bor.projects.minecraft.cratesystem.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import net.d_bor.library.files.ConfigFileSaver;

public class Messages {

	private static transient Messages values;
	private static transient final String fileName = "Config.json";
	private static transient File path;

	private HashMap<String, String> messages = new HashMap<>();

	private Messages() {

		messages.put("noperms", "Not enough Permission");
		messages.put("unknownarg", "Unbekanntes Argument");
		messages.put("unknownarglength", "Unbekannte Argumentenanzahl");
		messages.put("cratecreated", "Crate \"&Name\" wurde erstellt");
		messages.put("cratenotcreated", "Crate \"&Name\" existiert bereits");
		messages.put("cratedeleted", "Crate \"&Name\" wurde gelöscht");
		messages.put("cratenotdeleted", "Crate \"&Name\" existiert nicht");
		messages.put("cratedoesntexist", "Crate \"&Name\" existiert nicht");
		messages.put("onlyplayer", "Dieser Command darf nur von einem Spieler ausgeführt werden");
		messages.put("onlypossibleprobabilitys",
				"Die einzigen möglichen eingaben für die wahrscheinlichkeiten sind Fließkommazahlen und 'all' um den rest anzugeben um auf 100% zu kommen");
		messages.put("tohighprobability", "Die wahrscheinlichkeit, mit den vorherigen zusammenaddiert würde bei über 100% liegen");
		messages.put("itemadded","Das Item wurde zur Crate \"&Name\" hinzugefügt");
		messages.put("noiteminhand","Das item, welches zu der Crate hinzugefügt werden soll muss in der Hand gehalten werden");
		messages.put("crateplaced","Crate \"&Name\" platziert");
		messages.put("crateunplaced","Crate \"&Name\" entfernt");
		messages.put("nokey","Du hast keinen key fuer diese Crate");
		
	}

	public static String getMessage(String messageName) {

		return values.messages.get(messageName);

	}

	private static final boolean exists() {

		return values != null;

	}

	public static final boolean saveMessages() {

		if (exists()) {

			try {

				ConfigFileSaver.saveAsFile(path.toString(), fileName, values);
				return true;

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

		return false;

	}

	public static final boolean loadMessages(File path) {

		if (!exists()) {

			try {

				Messages.path = path;
				values = ConfigFileSaver.loadFromFile(path.toString(), fileName, Messages.class);

			} catch (FileNotFoundException e) {

				values = new Messages();
				saveMessages();

			}

			return true;

		}

		return false;

	}

}
