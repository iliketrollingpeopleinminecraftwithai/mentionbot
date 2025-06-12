package com.mentionbot;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ChatMessageCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MentionBotMod implements ModInitializer {
    private static final String BOT_NAME = "Nul1_YT";
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final String OLLAMA_MODEL = "your-ollama-model";
    private final OkHttpClient http = new OkHttpClient();
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void onInitialize() {
        ChatMessageCallback.EVENT.register((message, sender, signedMessage, timestamp) -> {
            String msg = message.getString();
            if (!msg.contains("@" + BOT_NAME)) return null;

            String user = sender.getName().getString();
            JsonObject payload = new JsonObject();
            payload.addProperty("model", OLLAMA_MODEL);
            payload.addProperty("prompt", 
                "User " + user + " mentioned you in Minecraft chat:\n" +
                \"" + msg + "\"\n" +
                "How should Nul1_YT reply?");

            RequestBody body = RequestBody.create(payload.toString(), MediaType.get("application/json"));
            Request req = new Request.Builder().url(OLLAMA_URL).post(body).build();

            http.newCall(req).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response res) throws IOException {
                    if (!res.isSuccessful()) {
                        sendChat("System", "Ollama error: " + res.message());
                        return;
                    }

                    String text = JsonParser.parseString(res.body().string())
                                             .getAsJsonObject()
                                             .get("response")
                                             .getAsString()
                                             .trim();

                    sendChat(user, text);
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    sendChat("System", "Ollama request failed: " + e.getMessage());
                }
            });

            return null;
        });
    }

    private void sendChat(String toUser, String msg) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        mc.inGameHud.getChatHud()
          .addMessage(Text.of("@" + toUser + " " + msg), MessageType.CHAT, mc.player.getUuid());
    }
}
