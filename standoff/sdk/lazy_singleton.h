//
// Created by wergity on 07.04.2025.
//

#ifndef LAZY_SINGLETON_H
#define LAZY_SINGLETON_H

#include "sdk/sdk.h"

struct player_manager;

namespace lazy_singleton
{
  player_manager* get_player_manager();
}

#endif //LAZY_SINGLETON_H