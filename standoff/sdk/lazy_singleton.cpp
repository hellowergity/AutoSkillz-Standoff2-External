//
// Created by wergity on 07.04.2025.
//

#include "lazy_singleton.h"

player_manager* lazy_singleton::get_player_manager()
{
  const std::uintptr_t type_info = memory::read(memory::game_library + player_manager_type_info);
  if (!type_info) {
    return nullptr;
  }

  const std::uintptr_t parent = memory::read(type_info + offsetof(il2cpp_class_t, parent));
  if (!parent) {
    return nullptr;
  }

  const std::uintptr_t static_fields = memory::read(parent + offsetof(il2cpp_class_t, static_fields));
  if (!static_fields) {
    return nullptr;
  }

  return memory::read<player_manager*>(static_fields);
}
