package net.fab3F;

import net.fab3F.threads.TokenGenerator;
import net.fab3F.threads.TokenSaver;
import org.json.JSONObject;

public class Main {

    public static int counter = 0;
    public static TokenSaver tokenSaver;
    public static TokenGenerator tokenGenerator;

    public static void main(String[] args) {

        System.out.println("Prozess gestartet.");

        ConfigReader cr = new ConfigReader("config.json");
        JSONObject config = cr.getConfig();
        if(config == null)
            return;

        int tokSavCapacity = config.getInt("tokenSaverCapacity");
        String outputFile = config.getString("outputFile");
        int amount = config.getInt("amount");
        String id = config.getString("partnerUserId");

        tokenSaver = new TokenSaver(tokSavCapacity, outputFile);
        tokenGenerator = new TokenGenerator(id);

        System.out.println("Token werden generiert.");

        while(counter < amount){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {}
            System.out.println(counter + " von " + amount);
        }

        System.out.println("Prozesse werden gestoppt.");

        tokenSaver.stopThread();
        tokenGenerator.stopThread();

        System.out.println("Token wurden generiert und in folgender Datei gespeichert: " + outputFile);

    }


}