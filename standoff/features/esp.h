//
// Created by wergity on 08.04.2025.
//

#ifndef ESP_H
#define ESP_H

#include "sdk/sdk.h"
#include "utils/vars.h"

namespace esp
{
  bool create(player_t* player, vector3_t world, vector3_t screen, std::uintptr_t camera);
}

#endif //ESP_H