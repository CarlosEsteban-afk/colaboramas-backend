package com.agora.message.service;

import com.agora.message.dto.MessageRequestDto;
import com.agora.message.dto.MessageResponseDto;
import com.agora.message.model.Message;
import com.agora.message.repository.MessageRepository;
import com.agora.user.dto.UserSummaryDTO;
import com.agora.user.model.User;
import com.agora.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository repo;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public MessageResponseDto sendMessage(MessageRequestDto dto) {
        // 1️⃣ Guardamos el mensaje en BD
        Message m = new Message();
        m.setFromUserId(dto.getFromUserId());
        m.setToUserId(dto.getToUserId());
        m.setSubject(dto.getSubject());
        m.setBody(dto.getMessage());
        m.setStatus("pending");
        m.setCreatedAt(Instant.now());
        m.setUpdatedAt(Instant.now());

        Message saved = repo.save(m);

        User destinatario = userRepository.findById(dto.getToUserId()).orElse(null);
        User remitente = userRepository.findById(dto.getFromUserId()).orElse(null);

        if (destinatario != null && destinatario.getEmail() != null) {
            String logoUrl = "https://logowik.com/content/uploads/images/agora7391.logowik.com.webp";
            String aceptarUrl = "https://miapp.com/messages/" + saved.getId() + "/respond?status=accepted";
            String rechazarUrl = "https://miapp.com/messages/" + saved.getId() + "/respond?status=rejected";

            String html = "<html>"
                    + "<body style='font-family:Arial,sans-serif;color:#333;'>"
                    + "<div style='text-align:center;'>"
                    + "<img src='" + logoUrl + "' alt='Agora' width='150' style='margin-bottom:20px;'/>"
                    + "<h2>¡Has recibido un mensaje en Agora!</h2>"
                    + "<p>De: <strong>" + (remitente != null ? remitente.getUsername() : "Usuario") + "</strong></p>"
                    + "<p>Asunto: <strong>" + dto.getSubject() + "</strong></p>"
                    + "<p>Mensaje: " + dto.getMessage() + "</p>"
                    + "<div style='margin-top:30px;'>"
                    + "<a href='" + aceptarUrl + "' style='padding:10px 20px;background-color:#4CAF50;color:white;text-decoration:none;margin-right:10px;border-radius:5px;'>Aceptar</a>"
                    + "<a href='" + rechazarUrl + "' style='padding:10px 20px;background-color:#f44336;color:white;text-decoration:none;border-radius:5px;'>Rechazar</a>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            emailService.enviarCorreo("o.millalonco05@gmail.com",
                    "Alguien te ha contactado en Agora",
                    html);
        }


        return toResponseDto(saved);
    }

    public List<MessageResponseDto> getReceived(Long userId) {
        return repo.findByToUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponseDto).toList();
    }


    public List<MessageResponseDto> getSent(Long userId) {
        return repo.findByFromUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponseDto).toList();
    }


    public MessageResponseDto respond(Long id, String status) {

        if (!status.equals("accepted") && !status.equals("rejected") && !status.equals("pending")) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        Message m = repo.findById(id).orElse(null);

        m.setStatus(status);
        m.setUpdatedAt(Instant.now());

        Message saved = repo.save(m);
        return toResponseDto(saved);
    }

    private MessageResponseDto toResponseDto(Message m) {
        MessageResponseDto r = new MessageResponseDto();
        r.setId(m.getId());
        r.setFromUserId(m.getFromUserId());
        r.setToUserId(m.getToUserId());
        r.setSubject(m.getSubject());
        r.setMessage(m.getBody());
        r.setStatus(m.getStatus());
        r.setCreatedAt(m.getCreatedAt());

        // Información resumida del remitente
        User remitente = userRepository.findById(m.getFromUserId()).orElse(null);
        if (remitente != null) {
            r.setFromUser(new UserSummaryDTO(
                    remitente.getId(),
                    remitente.getUsername(),
                    remitente.getEmail(),
                    remitente.getImageUrl()
            ));
        }

        // Información resumida del destinatario
        User destinatario = userRepository.findById(m.getToUserId()).orElse(null);
        if (destinatario != null) {
            r.setToUser(new UserSummaryDTO(
                    destinatario.getId(),
                    destinatario.getUsername(),
                    destinatario.getEmail(),
                    destinatario.getImageUrl()
            ));
        }

        return r;
    }

}
