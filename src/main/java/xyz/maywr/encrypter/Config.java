package xyz.maywr.encrypter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maywr
 * 24.05.2022 2:51
 */
public enum Config {
		INSTANCE;

	private JSONObject object;
	private boolean hasLoaded = false;
	private List<String> excludes = new ArrayList<>();

	public boolean saveDefault(File file) {
		if (file.exists()) return false;
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(getDefaultJson().toString(2));
			writer.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private JSONObject getDefaultJson() {
		JSONObject object = new JSONObject();
		JSONArray skippedClasses = new JSONArray();
		skippedClasses.put("com/package/");
		skippedClasses.put("com/package/Myclass");
		object.put("exclude", skippedClasses);
		object.put("key", "qwerty");
		return object;
	}

	public String getKey() {
		if (!hasLoaded) load();
		return object.getString("key");
	}

	public boolean shouldCrypt(String className) {
		if (!hasLoaded) load();
		for (String s : excludes) {
			if (className.equals(s)) return false;
			if (className.startsWith(s)) return false;
		}
		return true;
	}

	private void load() {
		try {
			object = new JSONObject(new String(Files.readAllBytes(Paths.get(Main.configFilePath)), StandardCharsets.UTF_8));
			for (int i = 0; i < object.getJSONArray("exclude").length(); i++) {
				excludes.add(object.getJSONArray("exclude").getString(i));
			}
			hasLoaded = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
