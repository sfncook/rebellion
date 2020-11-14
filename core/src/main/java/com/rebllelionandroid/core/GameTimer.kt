package com.rebllelionandroid.core

//import kotlinx.coroutines.delay
import javax.inject.Inject

class GameTimer @Inject constructor(): Runnable {
    var timeI = 1
    var running = false

    public fun start() {
        println("GameTimer.start")
        running = true
        this.start()
    }

    public fun stop() {
        println("GameTimer.stop")
        running = false
    }

    override fun run() {
        println("GameTimer.run")
        while(running) {
            println("GameTimer.run update " + timeI++)
            Thread.sleep(2000)
        }
    }


}