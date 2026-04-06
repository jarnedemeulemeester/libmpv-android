#define UTIL_EXTERN
#include "jni_utils.h"

#include <jni.h>
#include <cstdlib>
#include <mutex>

bool acquire_jni_env(JavaVM *vm, JNIEnv **env)
{
    int ret = vm->GetEnv((void**)env, JNI_VERSION_1_6);

    if (ret == JNI_EDETACHED) {
        return vm->AttachCurrentThread(env, nullptr) == JNI_OK;
    }

    return ret == JNI_OK;
}

static std::once_flag init_flag;

// Apparently it's considered slow to FindClass and GetMethodID every time we need them,
// so let's have a nice cache here
void init_methods_cache(JNIEnv *env) {
    std::call_once(init_flag, [env]() {
        auto find_and_ref = [&](const char* name) -> jclass {
            jclass localClass = env->FindClass(name);
            auto globalRef = reinterpret_cast<jclass>(env->NewGlobalRef(localClass));
            env->DeleteLocalRef(localClass);
            return globalRef;
        };

        java_Integer = find_and_ref("java/lang/Integer");
        java_Integer_init = env->GetMethodID(java_Integer, "<init>", "(I)V");
        java_Double = find_and_ref("java/lang/Double");
        java_Double_init = env->GetMethodID(java_Double, "<init>", "(D)V");
        java_Boolean = find_and_ref("java/lang/Boolean");
        java_Boolean_init = env->GetMethodID(java_Boolean, "<init>", "(Z)V");

        mpv_MPVLib = find_and_ref("dev/jdtech/mpv/MPVLib");
        mpv_MPVLib_eventProperty_S  = env->GetMethodID(mpv_MPVLib, "eventProperty", "(Ljava/lang/String;)V"); // eventProperty(String)
        mpv_MPVLib_eventProperty_Sb = env->GetMethodID(mpv_MPVLib, "eventProperty", "(Ljava/lang/String;Z)V"); // eventProperty(String, boolean)
        mpv_MPVLib_eventProperty_Sl = env->GetMethodID(mpv_MPVLib, "eventProperty", "(Ljava/lang/String;J)V"); // eventProperty(String, long)
        mpv_MPVLib_eventProperty_Sd = env->GetMethodID(mpv_MPVLib, "eventProperty", "(Ljava/lang/String;D)V"); // eventProperty(String, double)
        mpv_MPVLib_eventProperty_SS = env->GetMethodID(mpv_MPVLib, "eventProperty", "(Ljava/lang/String;Ljava/lang/String;)V"); // eventProperty(String, String)
        mpv_MPVLib_event = env->GetMethodID(mpv_MPVLib, "event", "(I)V"); // event(int)
        mpv_MPVLib_logMessage_SiS = env->GetMethodID(mpv_MPVLib, "logMessage", "(Ljava/lang/String;ILjava/lang/String;)V"); // logMessage(String, int, String)
    });
}
