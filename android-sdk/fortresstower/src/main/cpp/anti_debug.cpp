#include <jni.h>
#include <sys/ptrace.h>
#include <cerrno>

extern "C"
JNIEXPORT jboolean
Java_com_monomobile_fortresstower_native_NativeChecks_isTracerPresent(
        JNIEnv*,
        jobject) {
    errno = 0;
    ptrace(PTRACE_TRACEME, 0, nullptr, nullptr);

    if (errno != 0) {
        return JNI_TRUE;
    }

    ptrace(PTRACE_DETACH, 0, nullptr, nullptr);
    return JNI_FALSE;
}