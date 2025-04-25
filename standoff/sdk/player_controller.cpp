//
// Created by wergity on 07.04.2025.
//

#include "player_controller.h"

team_t player_controller::team()
{
  const auto pointer = reinterpret_cast<std::uintptr_t>(this);
  return memory::read<team_t>(pointer + offsetof(player_controller_t, team));
}

int player_controller::health()
{
  const auto pointer = reinterpret_cast<std::uintptr_t>(this);
  const std::uintptr_t photon_player = memory::read(pointer + offsetof(player_controller_t, player));
  if (!photon_player) {
    return 0;
  }

  const std::uintptr_t properties = memory::read(photon_player + offsetof(photon_player_t, custom_properties));
  if (!properties) {
    return 0;
  }

  const std::uintptr_t entries = memory::read(properties + offsetof(il2cpp_dictionary_t, entries));
  if (!entries) {
    return 0;
  }

  const int count = memory::read<int>(properties + offsetof(il2cpp_dictionary_t, count));

  for (int i = 0; i < count; i++) {
    const std::size_t index = i * sizeof(il2cpp_dictionary_t::entry_t);
    const std::uintptr_t entry = entries + offsetof(il2cpp_array_t, items) + index;
    if (!entry) {
      continue;
    }

    const std::uintptr_t key = memory::read(entry + offsetof(il2cpp_dictionary_t::entry_t, key));
    if (!key) {
      continue;
    }

    const int length = memory::read<int>(key + offsetof(string_t, length));
    if (!length) {
      continue;
    }

    std::vector<char> buffer(length * 2);
    if (!memory::read(key + offsetof(string_t, chars), buffer.data(), buffer.size())) {
      continue;
    }

    std::string key_data;
    key_data.reserve(length);
    for (int j = 0; j < length; j++) {
      if (const char symbol = buffer[j * 2]; symbol != '\0') {
        key_data.push_back(symbol);
      }
    }

    if (key_data == "health") {
      const std::uintptr_t value = memory::read(entry + offsetof(il2cpp_dictionary_t::entry_t, value));
      if (!value) {
        continue;
      }

      return memory::read<int>(value + offsetof(il2cpp_dictionary_t::entry_t, value));
    }
  }

  return 0;
}

bool player_controller::valid()
{
  const auto pointer = reinterpret_cast<std::uintptr_t>(this);
  return memory::read<bool>(pointer + offsetof(player_controller_t, is_character_visible));
}

std::uintptr_t player_controller::camera()
{
  const auto pointer = reinterpret_cast<std::uintptr_t>(this);
  const std::uintptr_t player_main_camera = memory::read(pointer + offsetof(player_controller_t, player_main_camera));
  if (!player_main_camera) {
    return 0;
  }

  return memory::read(player_main_camera + offsetof(player_main_camera_t, camera));
}

vector3_t player_controller::position()
{
  const auto pointer = reinterpret_cast<std::uintptr_t>(this);
  const std::uintptr_t transform = memory::read(pointer + offsetof(player_controller_t, transform));
  if (!transform) {
    return {};
  }

  return transform::position(transform);
}
