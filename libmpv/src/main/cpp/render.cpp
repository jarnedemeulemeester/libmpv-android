#include <jni.h>

#include <mpv/client.h>

#include "jni_utils.h"
#include "globals.h"

extern "C" {
    jni_func(void, nativeAttachSurface, jlong instance, jobject surface);
    jni_func(void, nativeDetachSurface, jlong instance);
}

jni_func(void, nativeAttachSurface, jlong instance, jobject surface) {
    auto mpv_instance = reinterpret_cast<MPVInstance*>(instance);
    mpv_instance->surface = env->NewGlobalRef(surface);
    int64_t wid = (int64_t)(intptr_t) mpv_instance->surface;
    mpv_set_option(mpv_instance->mpv, "wid", MPV_FORMAT_INT64, (void*) &wid);
}

jni_func(void, nativeDetachSurface, jlong instance) {
    auto mpv_instance = reinterpret_cast<MPVInstance*>(instance);
    int64_t wid = 0;
    mpv_set_option(mpv_instance->mpv, "wid", MPV_FORMAT_INT64, (void*) &wid);

    env->DeleteGlobalRef(mpv_instance->surface);
    mpv_instance->surface = nullptr;
}
