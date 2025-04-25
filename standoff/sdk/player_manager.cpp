//
// Created by wergity on 07.04.2025.
//

#include "player_manager.h"

player_controller* player_manager::controller()
{
  const auto pointer = reinterpret_cast<std::uintptr_t>(this);
  return memory::read<player_controller*>(pointer + offsetof(player_manager_t, controller));
}

void player_manager::for_each_players(const listener& for_each)
{
  const auto pointer = reinterpret_cast<std::uintptr_t>(this);

  const std::uintptr_t player_by_id = memory::read(pointer + offsetof(player_manager_t, player_by_id));
  if (!player_by_id) {
    return;
  }

  const std::uintptr_t entries = memory::read(player_by_id + offsetof(il2cpp_dictionary_t, entries));
  if (!entries) {
    return;
  }

  const int count = memory::read<int>(player_by_id + offsetof(il2cpp_dictionary_t, count));
  for (int i = 0; i < count; i++) {
    const std::size_t index = i * sizeof(il2cpp_dictionary_t::entry_t);
    const std::uintptr_t entry = entries + offsetof(il2cpp_array_t, items) + index;
    if (!entry) {
      continue;
    }

    const auto player = memory::read<player_controller*>(entry + offsetof(il2cpp_dictionary_t::entry_t, value));
    if (!player) {
      continue;
    }

    if (player->valid()) {
      for_each(player);
    }
  }
}