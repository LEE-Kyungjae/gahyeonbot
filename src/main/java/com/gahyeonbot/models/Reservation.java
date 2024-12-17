package com.gahyeonbot.models;

import java.util.concurrent.ScheduledFuture;

public class Reservation {
    private final long id;
    private final long memberId;
    private final String memberName;
    private final long guildId;
    private final ScheduledFuture<?> task;
    private final String description;

    public Reservation(long id, long memberId, String memberName, long guildId, ScheduledFuture<?> task, String description) {
        this.id = id;
        this.memberId = memberId;
        this.memberName = memberName;
        this.guildId = guildId;
        this.task = task;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public long getGuildId() {
        return guildId;
    }

    public ScheduledFuture<?> getTask() {
        return task;
    }

    public String getDescription() {
        return description;
    }
}