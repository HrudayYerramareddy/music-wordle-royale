package com.wordleroyale.backend.service;

import com.wordleroyale.backend.model.GameRoom;
import com.wordleroyale.backend.model.Player;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private final Map<String, GameRoom> rooms = new ConcurrentHashMap<>();

    public GameRoom getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public GameRoom getOrCreateRoom(String roomId) {
        return rooms.computeIfAbsent(roomId, GameRoom::new);
    }

    public Player joinRoom(String roomId, String playerName) {
        if (playerName == null || playerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }

        GameRoom room = getOrCreateRoom(roomId);

        Player player = new Player(UUID.randomUUID().toString(), playerName.trim());
        room.addPlayer(player); // throws if started
        return player;
    }

    public void startRoom(String roomId, String hostPlayerId) {
        GameRoom room = getRoom(roomId);
        if (room == null) throw new IllegalStateException("Room not found");
        if (room.isStarted()) throw new IllegalStateException("Game already started");
        if (hostPlayerId == null || hostPlayerId.isBlank()) throw new IllegalArgumentException("hostPlayerId required");

        // Host rule: if no host set yet, whoever starts becomes host
        if (room.getHostPlayerId() == null) {
            room.setHostPlayerId(hostPlayerId);
        } else if (!room.getHostPlayerId().equals(hostPlayerId)) {
            throw new SecurityException("Only host can start game");
        }

        room.start();
    }

    // Drain HP only after start
    @Scheduled(fixedRate = 1000)
    public void drainHpTick() {
        for (GameRoom room : rooms.values()) {
            if (!room.isStarted()) continue;

            for (Player p : room.getPlayers()) {
                p.drain(1);
            }
        }
    }
}
