//
// Created by wergity on 07.04.2025.
//

#ifndef LOGCAT_H
#define LOGCAT_H

#define LOGCAT_TAG "KittenWare"

namespace logcat
{
  void info(const char* fmt, ...);

  void error(const char* fmt, ...);

  void warning(const char* fmt, ...);
}

#endif //LOGCAT_H