//
// Created by wergity on 08.04.2025.
//

#pragma once

#define MAX_PLAYERS_IN_GAME  10

struct preferences_t
{
  float screen_width;
  float screen_height;

  bool aimbot;
  bool silent;
  int  smoothness;
  bool show_fov;
  int  fov_width;
  int  fov_height;
  bool visibility_checks;
  bool ignore_off_screen;
  int  target_selection;
};

struct rect_t
{
  float x;
  float y;
  float w;
  float h;
};

struct player_t
{
  bool   enemy;
  int    health;
  float  distance;
  rect_t location;
};

struct player_list_t
{
  int      count;
  player_t array[MAX_PLAYERS_IN_GAME];
};

static preferences_t preferences{};