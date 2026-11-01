#pragma once

#include <android/log.h>
#include <jni.h>

#include <mpv/client.h>

#define DEBUG 1

#define LOG_TAG "mpv"
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#if DEBUG
#define ALOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#else
#define ALOGV(...) (void)0
#endif

// Maps an mpv log level (MPV_LOG_LEVEL_*) to the corresponding Android log
// priority, so mpv messages show up with their real severity in logcat
// instead of everything being logged as verbose.
static inline android_LogPriority mpv_level_to_android(int level) {
    switch (level) {
    case MPV_LOG_LEVEL_FATAL: return ANDROID_LOG_FATAL;
    case MPV_LOG_LEVEL_ERROR: return ANDROID_LOG_ERROR;
    case MPV_LOG_LEVEL_WARN:  return ANDROID_LOG_WARN;
    case MPV_LOG_LEVEL_INFO:  return ANDROID_LOG_INFO;
    case MPV_LOG_LEVEL_V:     return ANDROID_LOG_DEBUG;
    default:                  return ANDROID_LOG_VERBOSE; // debug, trace, unknown
    }
}

void die(JNIEnv *env, const char *msg);
void throw_java_exception(JNIEnv *env, const char *msg);
