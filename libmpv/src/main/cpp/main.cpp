#include <jni.h>
#include <cstdlib>
#include <cstdio>
#include <ctime>
#include <clocale>
#include <atomic>

#include <mpv/client.h>

#include <pthread.h>

extern "C" {
    #include <libavcodec/jni.h>
}

#include "log.h"
#include "jni_utils.h"
#include "event.h"
#include "globals.h"

#define ARRAYLEN(a) (sizeof(a)/sizeof(a[0]))

// Update the JNI functions to accept a jlong parameter, which will be a
// pointer to the MPVInstance
extern "C" {
    jni_func(jlong, nativeCreate, jobject thiz, jobject appctx);
    jni_func(void, nativeInit, jlong instance);
    jni_func(void, nativeDestroy, jlong instance);
    jni_func(void, nativeCommand, jlong instance, jobjectArray jarray);
};

static void prepare_environment(JNIEnv *env, jobject appctx, MPVInstance* instance) {
    setlocale(LC_NUMERIC, "C");

    if (!env->GetJavaVM(&instance->vm) && instance->vm)
        av_jni_set_java_vm(instance->vm, nullptr);
    if (!instance->methods_initialized) {
        instance->methods_initialized = init_methods_cache(env, instance->javaObject);
    }
}

jni_func(jlong, nativeCreate, jobject thiz, jobject appctx) {
    auto instance = new MPVInstance();
    instance->event_thread_request_exit = false;
    instance->javaObject = env->NewGlobalRef(thiz);
    prepare_environment(env, appctx, instance);

    instance->mpv = mpv_create();
    if (!instance->mpv) {
        delete instance;
        die("context init failed");
    }

    mpv_request_log_messages(instance->mpv, "v");
    return reinterpret_cast<jlong>(instance);
}

jni_func(void, nativeInit, jlong instance) {
    auto mpv_instance = reinterpret_cast<MPVInstance*>(instance);
    if (!mpv_instance->mpv)
        die("mpv is not created");

    if (mpv_initialize(mpv_instance->mpv) < 0)
        die("mpv init failed");

    mpv_instance->event_thread_request_exit = false;
    pthread_create(&mpv_instance->event_thread_id, nullptr, event_thread, mpv_instance);
}

jni_func(void, nativeDestroy, jlong instance) {
    auto mpv_instance = reinterpret_cast<MPVInstance*>(instance);
    if (!mpv_instance->mpv)
        die("mpv destroy called but it's already destroyed");

    mpv_instance->event_thread_request_exit = true;
    mpv_wakeup(mpv_instance->mpv);
    pthread_join(mpv_instance->event_thread_id, nullptr);

    mpv_terminate_destroy(mpv_instance->mpv);
    env->DeleteGlobalRef(mpv_instance->javaObject);
    delete mpv_instance;
}

jni_func(void, nativeCommand, jlong instance, jobjectArray jarray) {
    auto mpv_instance = reinterpret_cast<MPVInstance*>(instance);
    const char *arguments[128] = { 0 };
    int len = env->GetArrayLength(jarray);
    if (!mpv_instance->mpv)
        die("Cannot run command: mpv is not initialized");
    if (len >= ARRAYLEN(arguments))
        die("Cannot run command: too many arguments");

    for (int i = 0; i < len; ++i)
        arguments[i] = env->GetStringUTFChars((jstring)env->GetObjectArrayElement(jarray, i), nullptr);

    mpv_command(mpv_instance->mpv, arguments);

    for (int i = 0; i < len; ++i)
        env->ReleaseStringUTFChars((jstring)env->GetObjectArrayElement(jarray, i), arguments[i]);
}
