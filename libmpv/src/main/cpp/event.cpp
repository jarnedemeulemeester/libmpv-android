#include <jni.h>

#include <mpv/client.h>

#include "globals.h"
#include "jni_utils.h"
#include "log.h"


static void sendPropertyUpdateToJava(JNIEnv *env, MPVInstance* instance, mpv_event_property *prop) {
    jstring jprop = env->NewStringUTF(prop->name);
    jstring jvalue = nullptr;
    switch (prop->format) {
    case MPV_FORMAT_NONE:
        env->CallVoidMethod(instance->javaObject, mpv_MPVLib_eventProperty_S, jprop);
        break;
    case MPV_FORMAT_FLAG:
        env->CallVoidMethod(instance->javaObject, mpv_MPVLib_eventProperty_Sb, jprop, *(int*)prop->data);
        break;
    case MPV_FORMAT_INT64:
        env->CallVoidMethod(instance->javaObject, mpv_MPVLib_eventProperty_Sl, jprop, *(int64_t*)prop->data);
        break;
    case MPV_FORMAT_DOUBLE:
        env->CallVoidMethod(instance->javaObject, mpv_MPVLib_eventProperty_Sd, jprop, *(double*)prop->data);
        break;
    case MPV_FORMAT_STRING:
        jvalue = env->NewStringUTF(*(const char**)prop->data);
        env->CallVoidMethod(instance->javaObject, mpv_MPVLib_eventProperty_SS, jprop, jvalue);
        break;
    default:
        ALOGV("sendPropertyUpdateToJava: Unknown property update format received in callback: %d!", prop->format);
        break;
    }
    if (jprop)
        env->DeleteLocalRef(jprop);
    if (jvalue)
        env->DeleteLocalRef(jvalue);
}

static void sendEventToJava(JNIEnv *env, MPVInstance* instance, int event) {
    env->CallVoidMethod(instance->javaObject, mpv_MPVLib_event, event);
}

static inline bool invalid_utf8(unsigned char c) {
    return c == 0xc0 || c == 0xc1 || c >= 0xf5;
}

static void sendLogMessageToJava(JNIEnv *env, MPVInstance* instance, mpv_event_log_message *msg) {
    // filter the most obvious cases of invalid utf-8
    int invalid = 0;
    for (int i = 0; msg->text[i]; i++)
        invalid |= invalid_utf8((unsigned char) msg->text[i]);
    if (invalid)
        return;

    jstring jprefix = env->NewStringUTF(msg->prefix);
    jstring jtext = env->NewStringUTF(msg->text);

    env->CallVoidMethod(instance->javaObject, mpv_MPVLib_logMessage_SiS,
        jprefix, (jint) msg->log_level, jtext);

    if (jprefix)
        env->DeleteLocalRef(jprefix);
    if (jtext)
        env->DeleteLocalRef(jtext);
}

void *event_thread(void *arg) {
    auto instance = static_cast<MPVInstance*>(arg);
    JNIEnv *env = nullptr;
    acquire_jni_env(instance->vm, &env);
    if (!env)
        die("failed to acquire java env");

    while (true) {
        mpv_event *mp_event;
        mpv_event_property *mp_property;
        mpv_event_log_message *msg;

        mp_event = mpv_wait_event(instance->mpv, -1.0);

        if (instance->event_thread_request_exit)
            break;

        if (mp_event->event_id == MPV_EVENT_NONE)
            continue;

        switch (mp_event->event_id) {
        case MPV_EVENT_LOG_MESSAGE:
            msg = (mpv_event_log_message*)mp_event->data;
            ALOGV("[%s:%s] %s", msg->prefix, msg->level, msg->text);
            sendLogMessageToJava(env, instance, msg);
            break;
        case MPV_EVENT_PROPERTY_CHANGE:
            mp_property = (mpv_event_property*)mp_event->data;
            sendPropertyUpdateToJava(env, instance, mp_property);
            break;
        default:
            ALOGV("event: %s\n", mpv_event_name(mp_event->event_id));
            sendEventToJava(env, instance, mp_event->event_id);
            break;
        }
    }

    instance->vm->DetachCurrentThread();

    return nullptr;
}
