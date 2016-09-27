LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := $(call all-subdir-java-files)
LOCAL_PACKAGE_NAME := RemoteAndroidServer
LOCAL_CERTIFICATE := platform
#LOCAL_CERTIFICATE := share
LOCAL_OVERRIDES_PACKAGES := Home
include $(BUILD_PACKAGE)