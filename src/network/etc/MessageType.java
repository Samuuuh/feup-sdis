package network.etc;

public enum MessageType {
    // Protocol messages
    DELETE,

    // Backup protocol
    BACKUP,
    DONE_BACKUP,
    STORED,

    // Restore protocol
    RESTORE,
    RCV_RESTORE,

    // Reclaim protocol
    RECLAIM,

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
