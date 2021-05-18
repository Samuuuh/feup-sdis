package network.etc;

public enum MessageType {
    // Protocol messages
    BACKUP,
    RESTORE,
    DELETE,
    RECLAIM,

    DONE_BACKUP,
    
    // Join
    LOOKUP,
    SUCCESSOR,
    NOTIFY,

    // Fix Fingers
    FIX_FINGERS,
    ANS_FIX_FINGERS,

    // Stabilize
    GET_PREDECESSOR,
    ANS_GET_PREDECESSOR,

    // Generic messages
    OK
}
