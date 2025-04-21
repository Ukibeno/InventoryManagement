package InventoryManagement.model;

public enum Status {
    PENDING,      // Order created by the manager.
    EDITED,    // Admin has edited the order
    APPROVED,
    REJECTED,
    COMPLETED     // Order has been completed.
}
