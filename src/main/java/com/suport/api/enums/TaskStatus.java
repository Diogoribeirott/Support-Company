package com.suport.api.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum TaskStatus {
    OPEN,
    IN_PROGRESS,
    COMPLETED,
    CLOSED
}