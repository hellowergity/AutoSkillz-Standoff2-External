//
// Created by wergity on 08.04.2025.
//

#pragma once

#if defined(__LP64__) || defined(__aarch64__) || defined(__x86_64__)
#define IS_64_BIT true
#else
#define IS_64_BIT false
#endif

#define PADDING(type, name, x32, x64)               \
  char name##_padding[(IS_64_BIT ? (x64) : (x32))]; \
  type name

#define player_manager_type_info (IS_64_BIT ? 0x699EEE8 : 0x0)

#include <elf.h>
#include <string>

#include <cstdint>
#include <fstream>
#include <sys/uio.h>

#include <cstddef>
#include <functional>

#include "sdk/engine/memory.h"

#include "sdk/engine/il2cpp.h"
#include "sdk/engine/unity.h"

#include "sdk/engine/camera.h"
#include "sdk/engine/transform.h"

#include "sdk/player_controller.h"
#include "sdk/player_manager.h"

#include "sdk/lazy_singleton.h"