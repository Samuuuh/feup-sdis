package network.etc;

public enum MessageType {
    // Protocol messages
    BACKUP,
    
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
