//
// Created by wergity on 07.04.2025.
//

#ifndef PLAYER_MANAGER_H
#define PLAYER_MANAGER_H

#include "sdk/sdk.h"

struct player_controller;

struct player_manager_t
{
  PADDING(int, player_view_id, 0x0, 0x24);
  void* player_by_id;
  void* EDECEHAAHEDGGCG; // Dictionary<int, int>
  void* GHFEGCDHFCAGCCF; // HashSet<PlayerController>
  void* AGEBFDFEDABABDC; // Dictionary<string, Material>
  void* listeners; // HashSet<HEBBHGDCEGBEBAG>
  void* player_on_set_snapshot_event; // Action<PlayerController, CDDHFAAAEEFDHCE>
  void* BABBGHBHGBHAAGD; // List<Type>
  bool  DFDABCCAAEACBBC;
  void* DGBHCAAGACGAGGF;
  void* controller;
  void* pools;
};

struct player_manager
{
  using listener = std::function<void(player_controller* player)>;

  player_controller* controller();

  void for_each_players(const listener& for_each);
};

#endif //PLAYER_MANAGER_H