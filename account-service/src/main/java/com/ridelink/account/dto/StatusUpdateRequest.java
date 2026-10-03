package com.ridelink.account.dto;

import com.ridelink.account.model.AccountStatus;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateRequest {

    @NotNull(message = "Status is required")
    private AccountStatus status;

    public StatusUpdateRequest() {}

    public StatusUpdateRequest(AccountStatus status) {
        this.status = status;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
