package com.agora.admin.dto;

import com.agora.event.model.EventType;

public class ChangeEventTypeRequest {
    private EventType type;

    public ChangeEventTypeRequest() {
    }

    public ChangeEventTypeRequest(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }
}
