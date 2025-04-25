//
// Created by wergity on 07.04.2025.
//

#ifndef PLAYER_CONTROLLER_H
#define PLAYER_CONTROLLER_H

#include "sdk/sdk.h"

enum team_t : uint8_t
{
  team_none,
  team_tr,
  team_ct
};

struct player_controller_t
{
  PADDING(void*, main_camera_holder, 0x0, 0x28);
  void*  fps_camera_holder;
  void*  fps_directive;
  void*  player_level_zones_controller;
  void*  CBBCADGCDCDGEBA; // PlayerCharacterView
  void*  FAFCECGDHBHAFDC; // PlayerCharacterView
  bool   GHGEDHHHDHEBGGG;
  team_t team;
  float  AFBHGABBDADGFHA;
  void*  aim_controller;
  void*  weaponry_controller;
  void*  mecanim_controller;
  void*  movement_controller;
  void*  arms_animation_controller;
  void*  player_hit_controller;
  void*  player_occlusion_controller;
  void*  network_controller;
  void*  arms_lod_group;
  void*  HFHBFHEAAGCFHBG; // PlayerCharacterView
  bool   is_character_visible;
  bool   HCFBAAAFFFHDADH;
  float  BDECFACABGHFCCG;
  void*  player_sound_controller;
  void*  player_main_camera;
  void*  player_fps_camera;
  void*  player_marker_trigger;
  void*  transform;
  void*  controller;
  void*  CAFHEGDEFBHAGAE; // Dictionary<Type, Controller>
  void*  character_controller;
  void*  skinned_mesh_lod_group;
  void*  character_lod_group;
  bool   CBHEEFCEGGHFBAE;
  bool   GAAAEAFCCAAACFA;
  int    view_mode;
  void*  ABFEGCDFEDBAEAA; // Nullable<int>
  void*  AHCDHHGCDGHAHFE; // Nullable<bool>
  void*  FEDHCFBEBBBAFFG; // Nullable<int>
  void*  photon_view;
  int    AEFHEEAAHCDCHED;
  int    EACADABHEEEBBDD;
  void*  player;
};

struct player_main_camera_t
{
  PADDING(void*, camera, 0x0, 0x20);
};

struct photon_player_t
{
  PADDING(void*, logger, 0x0, 0x10);
  int   actor_id;
  void* name_field;
  void* user_id;
  bool  is_local;
  bool  is_inactive;
  void* custom_properties;
};

struct player_controller
{
  team_t team();

  int health();

  bool valid();

  std::uintptr_t camera();

  vector3_t position();
};

#endif //PLAYER_CONTROLLER_H