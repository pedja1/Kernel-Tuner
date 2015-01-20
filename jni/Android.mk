LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)


LOCAL_MODULE    := linpack-jni
#LOCAL_SRC_FILES := linpack-jni.c
	
ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
    LOCAL_CFLAGS : += -DHAVE_NEON=1 
    LOCAL_SRC_FILES := linpack-neon.c.neon
else
    LOCAL_SRC_FILES := linpack-standard.c
endif

LOCAL_STATIC_LIBRARIES := cpufeatures
LOCAL_LDFLAGS := -llog

include $(BUILD_SHARED_LIBRARY)
$(call import-module,cpufeatures)
