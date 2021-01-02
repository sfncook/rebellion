package com.rebllelionandroid.core.gameUpdater.events

data class MissionFailureEvent(val missionDescription: String): UpdateEvent {
    override fun getEventMessage() = "Mission failed: ${missionDescription}."
}