package com.rebllelionandroid.core.gameUpdater.events

data class MissionSuccessEvent(val missionDescription: String): UpdateEvent {
    override fun getEventMessage() = "Mission success: ${missionDescription}."
}