//
// Created by wergity on 07.04.2025.
//

#ifndef MEMORY_H
#define MEMORY_H

#include "sdk/sdk.h"

namespace memory
{
  extern int game_pid;

  extern std::uintptr_t game_library;

  bool find_process();

  bool find_library();

  bool read(std::uintptr_t address, void* buffer, std::size_t length);

  template <typename t = std::uintptr_t>
  t read(const std::uintptr_t address)
  {
    t result;
    memory::read(address, &result, sizeof(t));
    return result;
  }

  bool initialize();
}

#endif //MEMORY_H