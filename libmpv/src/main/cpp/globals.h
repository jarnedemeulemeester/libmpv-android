#pragma once

#include <atomic>

// create a new struct to hold the mpv_handle and other related data
struct MPVInstance {
    mpv_handle *mpv;
    JavaVM *vm;
    pthread_t event_thread_id;
    std::atomic<bool> event_thread_request_exit;
    jobject javaObject;
    bool methods_initialized;
    jobject surface;
};
