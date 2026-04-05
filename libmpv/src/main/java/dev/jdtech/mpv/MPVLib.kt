package dev.jdtech.mpv

import android.content.Context
import android.view.Surface

@Suppress("unused")
class MPVLib {
    private var nativeInstance: Long = 0
    private val observers = mutableListOf<EventObserver>()
    private val logObservers = mutableListOf<LogObserver>()

    companion object {
        init {
            val libs = arrayOf("mpv", "player")
            for (lib in libs) {
                System.loadLibrary(lib)
            }
        }
    }

    fun create(appctx: Context) {
        nativeInstance = nativeCreate(this, appctx)
    }
    private external fun nativeCreate(thiz: MPVLib, appctx: Context): Long

    fun init() {
        nativeInit(nativeInstance)
    }
    private external fun nativeInit(instance: Long)

    fun destroy() {
        nativeDestroy(nativeInstance)
        nativeInstance = 0
    }
    private external fun nativeDestroy(instance: Long)

    fun attachSurface(surface: Surface) {
        nativeAttachSurface(nativeInstance, surface)
    }
    private external fun nativeAttachSurface(instance: Long, surface: Surface)

    fun detachSurface() {
        nativeDetachSurface(nativeInstance)
    }
    private external fun nativeDetachSurface(instance: Long)

    fun command(cmd: Array<String>) {
        nativeCommand(nativeInstance, cmd)
    }
    private external fun nativeCommand(instance: Long, cmd: Array<String>)

    fun setOptionString(name: String, value: String): Int {
        return nativeSetOptionString(nativeInstance, name, value)
    }
    private external fun nativeSetOptionString(instance: Long, name: String, value: String): Int

    fun getPropertyInt(property: String): Int? {
        return nativeGetPropertyInt(nativeInstance, property)
    }
    private external fun nativeGetPropertyInt(instance: Long, property: String): Int?

    fun setPropertyInt(property: String, value: Int) {
        nativeSetPropertyInt(nativeInstance, property, value)
    }
    private external fun nativeSetPropertyInt(instance: Long, property: String, value: Int): Int

    fun getPropertyDouble(property: String): Double? {
        return nativeGetPropertyDouble(nativeInstance, property)
    }
    private external fun nativeGetPropertyDouble(instance: Long, property: String): Double?

    fun setPropertyDouble(property: String, value: Double) {
        nativeSetPropertyDouble(nativeInstance, property, value)
    }
    private external fun nativeSetPropertyDouble(instance: Long, property: String, value: Double)

    fun getPropertyBoolean(property: String): Boolean? {
        return nativeGetPropertyBoolean(nativeInstance, property)
    }
    private external fun nativeGetPropertyBoolean(instance: Long, property: String): Boolean?

    fun setPropertyBoolean(property: String, value: Boolean) {
        nativeSetPropertyBoolean(nativeInstance, property, value)
    }
    private external fun nativeSetPropertyBoolean(instance: Long, property: String, value: Boolean)

    fun getPropertyString(property: String): String? {
        return nativeGetPropertyString(nativeInstance, property)
    }
    private external fun nativeGetPropertyString(instance: Long, property: String): String?

    fun setPropertyString(property: String, value: String) {
        nativeSetPropertyString(nativeInstance, property, value)
    }
    private external fun nativeSetPropertyString(instance: Long, property: String, value: String)

    fun observeProperty(property: String, format: Int) {
        nativeObserveProperty(nativeInstance, property, format)
    }
    private external fun nativeObserveProperty(instance: Long, property: String, format: Int)

    fun addObserver(o: EventObserver) {
        synchronized(observers) {
            observers.add(o)
        }
    }

    fun removeObserver(o: EventObserver) {
        synchronized(observers) {
            observers.remove(o)
        }
    }

    fun eventProperty(property: String, value: Long) {
        synchronized(observers) {
            for (o in observers)
                o.eventProperty(property, value)
        }
    }

    fun eventProperty(property: String, value: Double) {
        synchronized(observers) {
            for (o in observers)
                o.eventProperty(property, value)
        }
    }

    fun eventProperty(property: String, value: Boolean) {
        synchronized(observers) {
            for (o in observers)
                o.eventProperty(property, value)
        }
    }

    fun eventProperty(property: String, value: String) {
        synchronized(observers) {
            for (o in observers)
                o.eventProperty(property, value)
        }
    }

    fun eventProperty(property: String) {
        synchronized(observers) {
            for (o in observers)
                o.eventProperty(property)
        }
    }

    fun event(eventId: Int) {
        synchronized(observers) {
            for (o in observers)
                o.event(eventId)
        }
    }

    fun addLogObserver(o: LogObserver) {
        synchronized(logObservers) {
            logObservers.add(o)
        }
    }

    fun removeLogObserver(o: LogObserver) {
        synchronized(logObservers) {
            logObservers.remove(o)
        }
    }

    fun logMessage(prefix: String, level: Int, text: String) {
        synchronized(logObservers) {
            for (o in logObservers)
                o.logMessage(prefix, level, text)
        }
    }

    interface EventObserver {
        fun eventProperty(property: String)
        fun eventProperty(property: String, value: Long)
        fun eventProperty(property: String, value: Double)
        fun eventProperty(property: String, value: Boolean)
        fun eventProperty(property: String, value: String)
        fun event(eventId: Int)
    }

    interface LogObserver {
        fun logMessage(prefix: String, level: Int, text: String)
    }

    object MpvFormat {
        const val MPV_FORMAT_NONE: Int = 0
        const val MPV_FORMAT_STRING: Int = 1
        const val MPV_FORMAT_OSD_STRING: Int = 2
        const val MPV_FORMAT_FLAG: Int = 3
        const val MPV_FORMAT_INT64: Int = 4
        const val MPV_FORMAT_DOUBLE: Int = 5
        const val MPV_FORMAT_NODE: Int = 6
        const val MPV_FORMAT_NODE_ARRAY: Int = 7
        const val MPV_FORMAT_NODE_MAP: Int = 8
        const val MPV_FORMAT_BYTE_ARRAY: Int = 9
    }

    object MpvEvent {
        const val MPV_EVENT_NONE: Int = 0
        const val MPV_EVENT_SHUTDOWN: Int = 1
        const val MPV_EVENT_LOG_MESSAGE: Int = 2
        const val MPV_EVENT_GET_PROPERTY_REPLY: Int = 3
        const val MPV_EVENT_SET_PROPERTY_REPLY: Int = 4
        const val MPV_EVENT_COMMAND_REPLY: Int = 5
        const val MPV_EVENT_START_FILE: Int = 6
        const val MPV_EVENT_END_FILE: Int = 7
        const val MPV_EVENT_FILE_LOADED: Int = 8
        @Deprecated("")
        const val MPV_EVENT_IDLE: Int = 11
        @Deprecated("")
        const val MPV_EVENT_TICK: Int = 14
        const val MPV_EVENT_CLIENT_MESSAGE: Int = 16
        const val MPV_EVENT_VIDEO_RECONFIG: Int = 17
        const val MPV_EVENT_AUDIO_RECONFIG: Int = 18
        const val MPV_EVENT_SEEK: Int = 20
        const val MPV_EVENT_PLAYBACK_RESTART: Int = 21
        const val MPV_EVENT_PROPERTY_CHANGE: Int = 22
        const val MPV_EVENT_QUEUE_OVERFLOW: Int = 24
        const val MPV_EVENT_HOOK: Int = 25
    }

    object MpvLogLevel {
        const val MPV_LOG_LEVEL_NONE: Int = 0
        const val MPV_LOG_LEVEL_FATAL: Int = 10
        const val MPV_LOG_LEVEL_ERROR: Int = 20
        const val MPV_LOG_LEVEL_WARN: Int = 30
        const val MPV_LOG_LEVEL_INFO: Int = 40
        const val MPV_LOG_LEVEL_V: Int = 50
        const val MPV_LOG_LEVEL_DEBUG: Int = 60
        const val MPV_LOG_LEVEL_TRACE: Int = 70
    }
}