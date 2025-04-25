//
// Created by wergity on 07.04.2025.
//

#include "logcat.h"

#include <android/log.h>

void logcat::info(const char *fmt, ...)
{
  va_list list;
  va_start(list, fmt);
  __android_log_vprint(ANDROID_LOG_INFO, LOGCAT_TAG, fmt, list);
  va_end(list);
}

void logcat::error(const char *fmt, ...)
{
  va_list list;
  va_start(list, fmt);
  __android_log_vprint(ANDROID_LOG_ERROR, LOGCAT_TAG, fmt, list);
  va_end(list);
}

void logcat::warning(const char *fmt, ...)
{
  va_list list;
  va_start(list, fmt);
  __android_log_vprint(ANDROID_LOG_WARN, LOGCAT_TAG, fmt, list);
  va_end(list);
}