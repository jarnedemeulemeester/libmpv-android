#include "jni_utils.h"

#include <jni.h>
#include <cstdlib>

bool acquire_jni_env(JavaVM *vm, JNIEnv **env)
{
    int ret = vm->GetEnv((void**) env, JNI_VERSION_1_6);
    if (ret == JNI_EDETACHED)
        return vm->AttachCurrentThread(env, nullptr) == 0;
    else
        return ret == JNI_OK;
}

// Apparently it's considered slow to FindClass and GetMethodID every time we need them,
// so let's have a nice cache here
jclass java_Integer, java_Double, java_Boolean;
jmethodID java_Integer_init, java_Integer_intValue, java_Double_init, java_Double_doubleValue, java_Boolean_init, java_Boolean_booleanValue;
jmethodID java_GLSurfaceView_requestRender;

jclass mpv_MPVLib;
jmethodID mpv_MPVLib_eventProperty_S, mpv_MPVLib_eventProperty_Sb, mpv_MPVLib_eventProperty_Sl, mpv_MPVLib_eventProperty_Sd, mpv_MPVLib_eventProperty_SS, mpv_MPVLib_event, mpv_MPVLib_logMessage_SiS;

bool init_methods_cache(JNIEnv *env, jobject instance) {
    #define FIND_CLASS(name) reinterpret_cast<jclass>(env->NewGlobalRef(env->FindClass(name)))
    java_Integer = FIND_CLASS("java/lang/Integer");
    java_Integer_init = env->GetMethodID(java_Integer, "<init>", "(I)V");
    java_Integer_intValue = env->GetMethodID(java_Integer, "intValue", "()I");
    java_Double = FIND_CLASS("java/lang/Double");
    java_Double_init = env->GetMethodID(java_Double, "<init>", "(D)V");
    java_Double_doubleValue = env->GetMethodID(java_Double, "doubleValue", "()D");
    java_Boolean = FIND_CLASS("java/lang/Boolean");
    java_Boolean_init = env->GetMethodID(java_Boolean, "<init>", "(Z)V");
    java_Boolean_booleanValue = env->GetMethodID(java_Boolean, "booleanValue", "()Z");

    mpv_MPVLib = env->GetObjectClass(instance);
    mpv_MPVLib_eventProperty_S  = env->GetMethodID(mpv_MPVLib, "eventProperty", "(Ljava/lang/String;)V"); // eventProperty(String)
    mpv_MPVLib_eventProperty_Sb = env->GetMethodID(mpv_MPVLib, "eventProperty", "(Ljava/lang/String;Z)V"); // eventProperty(String, boolean)
    mpv_MPVLib_eventProperty_Sl = env->GetMethodID(mpv_MPVLib, "eventProperty", "(Ljava/lang/String;J)V"); // eventProperty(String, long)
    mpv_MPVLib_eventProperty_Sd = env->GetMethodID(mpv_MPVLib, "eventProperty", "(Ljava/lang/String;D)V"); // eventProperty(String, double)
    mpv_MPVLib_eventProperty_SS = env->GetMethodID(mpv_MPVLib, "eventProperty", "(Ljava/lang/String;Ljava/lang/String;)V"); // eventProperty(String, String)
    mpv_MPVLib_event = env->GetMethodID(mpv_MPVLib, "event", "(I)V"); // event(int)
    mpv_MPVLib_logMessage_SiS = env->GetMethodID(mpv_MPVLib, "logMessage", "(Ljava/lang/String;ILjava/lang/String;)V"); // logMessage(String, int, String)
    #undef FIND_CLASS

    return true;
}
