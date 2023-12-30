package net.fab3F;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.nio.file.Paths;

public class ConfigReader {

    private final File file;

    public ConfigReader(String filePath){
        this.file = Paths.get(filePath).toFile();
    }

    public JSONObject getConfig() {
        try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            return new JSONObject(jsonContent.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
