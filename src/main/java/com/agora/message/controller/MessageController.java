package com.agora.message.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import java.util.List;

import com.agora.message.dto.MessageRequestDto;
import com.agora.message.dto.MessageResponseDto;
import com.agora.message.service.MessageService;
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<MessageResponseDto> send(
            @Valid @RequestBody MessageRequestDto dto
    ) {
        MessageResponseDto res = messageService.sendMessage(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/received/{userId}")
    public ResponseEntity<List<MessageResponseDto>> getReceived(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getReceived(userId));
    }

    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<MessageResponseDto>> getSent(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getSent(userId));
    }

    @PutMapping("/{id}/respond")
    public ResponseEntity<?> respond(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        // Validación de estados permitidos
        if (!status.equals("pending") &&
                !status.equals("accepted") &&
                !status.equals("rejected")) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid status: " + status + " (allowed: pending, accepted, rejected)");
        }
        MessageResponseDto res = messageService.respond(id, status);
        return ResponseEntity.ok(res);
    }
}
