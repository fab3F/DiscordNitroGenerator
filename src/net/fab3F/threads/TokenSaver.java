package net.fab3F.threads;

import net.fab3F.Main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class TokenSaver implements Runnable{

    public volatile boolean stop;
    private final Thread thread;

    public void stopThread(){
        this.stop = true;
        this.thread.interrupt();
    }

    private final Path filePath;

    private final ArrayBlockingQueue<String> tokensToSave;
    private final List<String> localList;

    public TokenSaver(int capacity, String filePath){
        this.filePath = Paths.get(filePath);
        this.tokensToSave = new ArrayBlockingQueue<>(capacity);
        this.localList = new ArrayList<String>();

        this.thread = new Thread(this, "TokenSaver");
        this.thread.start();
    }

    @Override
    public void run() {

        while (!stop) {

            if(!this.tokensToSave.isEmpty()){
                localList.clear();
                this.tokensToSave.drainTo(localList);


                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile(), true))) {
                    for (String token : localList) {
                        writer.write("https://discord.com/billing/partner-promotions/1180231712274387115/" + token + "\n");
                        Main.counter++;
                    }

                } catch (IOException ignored) {}

            }


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Thread.onSpinWait();
        }

    }

    public boolean addToken(String token){
        boolean added;
        try{
            added = this.tokensToSave.add(token);
        }catch (IllegalStateException exception){
            added = false;
        }
        return added;

    }

}
