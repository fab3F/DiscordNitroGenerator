package net.fab3F.threads;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import net.fab3F.Main;
import org.json.JSONObject;

public class TokenGenerator implements Runnable{

    public volatile boolean stop;
    private final Thread thread;

    public void stopThread(){
        this.stop = true;
        this.thread.interrupt();
    }

    String apiUrl = "https://api.discord.gx.games/v1/direct-fulfillment";
    String jsonPayload;
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request;

    public TokenGenerator(String id){
        this.jsonPayload = "{\"partnerUserId\":\"UUID\"}".replace("UUID", id);
        this.thread = new Thread(this, "TokenGenerator");
        this.thread.start();
    }

    private String tempToken = "";

    @Override
    public void run() {

        while(!stop){

            if(tempToken.isEmpty()){
                request = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .build();


                try {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    JSONObject jsonResponse = new JSONObject(response.body());
                    tempToken =  jsonResponse.getString("token");
                } catch (IOException | InterruptedException ignored) {}
            }

            if(Main.tokenSaver.addToken(tempToken)){
                tempToken = "";
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Thread.onSpinWait();
        }
    }

}
