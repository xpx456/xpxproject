LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
TARGET_PLATFORM := android-23
LOCAL_MODULE := GPIOControl
LOCAL_SRC_FILES := GPIOControl.c
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)