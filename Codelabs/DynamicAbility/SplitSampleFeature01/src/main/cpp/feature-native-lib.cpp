#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_huawei_android_dynamicfeaturesplit_splitsamplefeature01_FeatureActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello feature from C++";
    return env->NewStringUTF(hello.c_str());
}
