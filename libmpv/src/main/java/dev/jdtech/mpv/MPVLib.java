package dev.jdtech.mpv;

// Wrapper for native library

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Surface;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class MPVLib {

    static {
        String[] libs = {"mpv", "player"};
        for (String lib : libs) {
            System.loadLibrary(lib);
        }
    }

    public static native void create(Context appctx);

    public static native void init();

    public static native void destroy();

    public static native void attachSurface(Surface surface);

    public static native void detachSurface();

    public static native void command(@NonNull String[] cmd);

    public static native int setOptionString(@NonNull String name, @NonNull String value);

    public static native Integer getPropertyInt(@NonNull String property);

    public static native void setPropertyInt(@NonNull String property, @NonNull Integer value);

    public static native Double getPropertyDouble(@NonNull String property);

    public static native void setPropertyDouble(@NonNull String property, @NonNull Double value);

    public static native Boolean getPropertyBoolean(@NonNull String property);

    public static native void setPropertyBoolean(@NonNull String property, @NonNull Boolean value);

    public static native String getPropertyString(@NonNull String property);

    public static native void setPropertyString(@NonNull String property, @NonNull String value);

    public static native void observeProperty(@NonNull String property, @Format int format);

    private static final List<EventObserver> observers = new ArrayList<>();

    public static void addObserver(EventObserver o) {
        synchronized (observers) {
            observers.add(o);
        }
    }

    public static void removeObserver(EventObserver o) {
        synchronized (observers) {
            observers.remove(o);
        }
    }

    public static void eventProperty(String property, long value) {
        synchronized (observers) {
            for (EventObserver o : observers)
                o.eventProperty(property, value);
        }
    }

    public static void eventProperty(String property, double value) {
        synchronized (observers) {
            for (EventObserver o : observers)
                o.eventProperty(property, value);
        }
    }

    public static void eventProperty(String property, boolean value) {
        synchronized (observers) {
            for (EventObserver o : observers)
                o.eventProperty(property, value);
        }
    }

    public static void eventProperty(String property, String value) {
        synchronized (observers) {
            for (EventObserver o : observers)
                o.eventProperty(property, value);
        }
    }

    public static void eventProperty(String property) {
        synchronized (observers) {
            for (EventObserver o : observers)
                o.eventProperty(property);
        }
    }

    public static void event(@Event int eventId) {
        synchronized (observers) {
            for (EventObserver o : observers)
                o.event(eventId);
        }
    }

    private static final List<LogObserver> log_observers = new ArrayList<>();

    public static void addLogObserver(LogObserver o) {
        synchronized (log_observers) {
            log_observers.add(o);
        }
    }

    public static void removeLogObserver(LogObserver o) {
        synchronized (log_observers) {
            log_observers.remove(o);
        }
    }

    public static void logMessage(String prefix, @LogLevel int level, String text) {
        synchronized (log_observers) {
            for (LogObserver o : log_observers)
                o.logMessage(prefix, level, text);
        }
    }

    public interface EventObserver {
        void eventProperty(@NonNull String property);

        void eventProperty(@NonNull String property, long value);

        void eventProperty(@NonNull String property, double value);

        void eventProperty(@NonNull String property, boolean value);

        void eventProperty(@NonNull String property, @NonNull String value);

        void event(@Event int eventId);
    }

    public interface LogObserver {
        void logMessage(@NonNull String prefix, @LogLevel int level, @NonNull String text);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            MPV_FORMAT_NONE,
            MPV_FORMAT_STRING,
            MPV_FORMAT_OSD_STRING,
            MPV_FORMAT_FLAG,
            MPV_FORMAT_INT64,
            MPV_FORMAT_DOUBLE,
            MPV_FORMAT_NODE,
            MPV_FORMAT_NODE_ARRAY,
            MPV_FORMAT_NODE_MAP,
            MPV_FORMAT_BYTE_ARRAY
    })
    public @interface Format {}

    public static final int MPV_FORMAT_NONE = 0;
    public static final int MPV_FORMAT_STRING = 1;
    public static final int MPV_FORMAT_OSD_STRING = 2;
    public static final int MPV_FORMAT_FLAG = 3;
    public static final int MPV_FORMAT_INT64 = 4;
    public static final int MPV_FORMAT_DOUBLE = 5;
    public static final int MPV_FORMAT_NODE = 6;
    public static final int MPV_FORMAT_NODE_ARRAY = 7;
    public static final int MPV_FORMAT_NODE_MAP = 8;
    public static final int MPV_FORMAT_BYTE_ARRAY = 9;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            MPV_EVENT_NONE,
            MPV_EVENT_SHUTDOWN,
            MPV_EVENT_LOG_MESSAGE,
            MPV_EVENT_GET_PROPERTY_REPLY,
            MPV_EVENT_SET_PROPERTY_REPLY,
            MPV_EVENT_COMMAND_REPLY,
            MPV_EVENT_START_FILE,
            MPV_EVENT_END_FILE,
            MPV_EVENT_FILE_LOADED,
            MPV_EVENT_CLIENT_MESSAGE,
            MPV_EVENT_VIDEO_RECONFIG,
            MPV_EVENT_AUDIO_RECONFIG,
            MPV_EVENT_SEEK,
            MPV_EVENT_PLAYBACK_RESTART,
            MPV_EVENT_PROPERTY_CHANGE,
            MPV_EVENT_QUEUE_OVERFLOW,
            MPV_EVENT_HOOK
    })
    public @interface Event {}

    public static final int MPV_EVENT_NONE = 0;
    public static final int MPV_EVENT_SHUTDOWN = 1;
    public static final int MPV_EVENT_LOG_MESSAGE = 2;
    public static final int MPV_EVENT_GET_PROPERTY_REPLY = 3;
    public static final int MPV_EVENT_SET_PROPERTY_REPLY = 4;
    public static final int MPV_EVENT_COMMAND_REPLY = 5;
    public static final int MPV_EVENT_START_FILE = 6;
    public static final int MPV_EVENT_END_FILE = 7;
    public static final int MPV_EVENT_FILE_LOADED = 8;
    public static final int MPV_EVENT_CLIENT_MESSAGE = 16;
    public static final int MPV_EVENT_VIDEO_RECONFIG = 17;
    public static final int MPV_EVENT_AUDIO_RECONFIG = 18;
    public static final int MPV_EVENT_SEEK = 20;
    public static final int MPV_EVENT_PLAYBACK_RESTART = 21;
    public static final int MPV_EVENT_PROPERTY_CHANGE = 22;
    public static final int MPV_EVENT_QUEUE_OVERFLOW = 24;
    public static final int MPV_EVENT_HOOK = 25;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            MPV_LOG_LEVEL_NONE,
            MPV_LOG_LEVEL_FATAL,
            MPV_LOG_LEVEL_ERROR,
            MPV_LOG_LEVEL_WARN,
            MPV_LOG_LEVEL_INFO,
            MPV_LOG_LEVEL_V,
            MPV_LOG_LEVEL_DEBUG,
            MPV_LOG_LEVEL_TRACE
    })
    public @interface LogLevel {}

    public static final int MPV_LOG_LEVEL_NONE = 0;
    public static final int MPV_LOG_LEVEL_FATAL = 10;
    public static final int MPV_LOG_LEVEL_ERROR = 20;
    public static final int MPV_LOG_LEVEL_WARN = 30;
    public static final int MPV_LOG_LEVEL_INFO = 40;
    public static final int MPV_LOG_LEVEL_V = 50;
    public static final int MPV_LOG_LEVEL_DEBUG = 60;
    public static final int MPV_LOG_LEVEL_TRACE = 70;
}
