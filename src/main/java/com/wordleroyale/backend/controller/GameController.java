package com.wordleroyale.backend.controller;

import com.wordleroyale.backend.dto.JoinRequest;
import com.wordleroyale.backend.dto.StartRequest;
import com.wordleroyale.backend.model.GameRoom;
import com.wordleroyale.backend.model.Player;
import com.wordleroyale.backend.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/rooms")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<?> join(@PathVariable String roomId, @RequestBody JoinRequest req) {
        try {
            Player p = gameService.joinRoom(roomId, req.getName());
            return ResponseEntity.ok(Map.of(
                    "roomId", roomId,
                    "playerId", p.getId(),
                    "name", p.getName(),
                    "hp", p.getHp(),
                    "alive", p.isAlive()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("roomId", roomId, "error", e.getMessage()));
        }
    }

    @PostMapping("/{roomId}/start")
    public ResponseEntity<?> start(@PathVariable String roomId, @RequestBody StartRequest req) {
        try {
            gameService.startRoom(roomId, req.getHostPlayerId());
            return ResponseEntity.ok(Map.of("roomId", roomId, "started", true));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("roomId", roomId, "error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("roomId", roomId, "error", e.getMessage()));
        } catch (IllegalStateException e) {
            // room not found or already started
            String msg = e.getMessage();
            if ("Room not found".equals(msg)) {
                return ResponseEntity.status(404).body(Map.of("roomId", roomId, "error", msg));
            }
            return ResponseEntity.status(409).body(Map.of("roomId", roomId, "error", msg));
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable String roomId) {
        GameRoom room = gameService.getRoom(roomId);
        if (room == null) {
            return ResponseEntity.status(404).body(Map.of("roomId", roomId, "error", "Room not found"));
        }

        return ResponseEntity.ok(Map.of(
                "roomId", roomId,
                "started", room.isStarted(),
                "hostPlayerId", room.getHostPlayerId(),
                "players", room.getPlayers()
        ));
    }
}
