package com.example.kafkalearning.shared.contracts;

public final class EventTypes {

    public static final String USER_REGISTERED = "USER_REGISTERED";
    public static final String TASK_CREATED = "TASK_CREATED";
    public static final String TASK_STATUS_CHANGED = "TASK_STATUS_CHANGED";

    private EventTypes() {
    }
}
