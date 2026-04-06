#pragma once

#include <jni.h>

#define jni_func_name(name) Java_dev_jdtech_mpv_MPVLib_##name
#define jni_func(return_type, name, ...) JNIEXPORT return_type JNICALL jni_func_name(name) (JNIEnv *env, jobject obj, ##__VA_ARGS__)

bool acquire_jni_env(JavaVM *vm, JNIEnv **env);
void init_methods_cache(JNIEnv *env);

#ifndef UTIL_EXTERN
#define UTIL_EXTERN extern
#endif

UTIL_EXTERN jclass java_Integer, java_Double, java_Boolean;
UTIL_EXTERN jmethodID java_Integer_init, java_Double_init, java_Boolean_init;

UTIL_EXTERN jclass mpv_MPVLib;
UTIL_EXTERN jmethodID mpv_MPVLib_eventProperty_S,
        mpv_MPVLib_eventProperty_Sb,
        mpv_MPVLib_eventProperty_Sl,
        mpv_MPVLib_eventProperty_Sd,
        mpv_MPVLib_eventProperty_SS,
        mpv_MPVLib_event,
        mpv_MPVLib_logMessage_SiS;
