package network.etc;

public enum MessageType {
    // Protocol messages
    RECLAIM,

    // Backup protocol
    BACKUP,
    DONE_BACKUP,
    STORED,

    // Restore protocol
    RESTORE,
    RCV_RESTORE,
    
    // Delete protocol
    DELETE,

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
