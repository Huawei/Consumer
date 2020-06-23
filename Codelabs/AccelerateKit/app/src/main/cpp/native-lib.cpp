#include <jni.h>
#include <string>
#include <fcntl.h>
#include <android/log.h>
#include <dispatch/dispatch.h>

#define LOG_TAG "dispatch"

void dispatch_sample(void);

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_example_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */)
{
    std::string hello = "Hello from C++ dispatch!";
    dispatch_autostat_enable(env);
    dispatch_sample();
    return env->NewStringUTF(hello.c_str());
}

void dispatch_sample(void)
{
    int i;
    dispatch_queue_t concurr_q = dispatch_queue_create("concurrent", DISPATCH_QUEUE_CONCURRENT);
    dispatch_queue_t serial_q = dispatch_queue_create("serial", DISPATCH_QUEUE_SERIAL);

    for (i = 0; i < 4; i++) {
        dispatch_async(concurr_q, ^{
            __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "task %d is running in a concurrent queue", i);
        });
    }

    dispatch_barrier_sync(concurr_q, ^{
        __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "all tasks in the concurrent queue has finished");
    });

    for (i = 4; i < 8; i++) {
        dispatch_async(serial_q, ^{
            __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "task %d is running in a serial queue", i);
        });
    }

    dispatch_sync(serial_q, ^{
        __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "all tasks in the serial queue has finished");
    });

    dispatch_release(concurr_q);
    dispatch_release(serial_q);
}
