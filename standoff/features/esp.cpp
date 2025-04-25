//
// Created by wergity on 08.04.2025.
//

#include "esp.h"

bool esp::create(player_t* player, vector3_t world, const vector3_t screen, const std::uintptr_t camera)
{
  if (camera::is_out_of_screen(screen)) {
    return false;
  }

  world.y += 1.7f;
  const vector3_t head = camera::world_to_screen(camera, world);

  const float height = head.y - screen.y;
  const float width = height / 2;

  const rect_t location = {
    screen.x,
    screen.y,
    width,
    height
  };

  player->location = location;
  return true;
}