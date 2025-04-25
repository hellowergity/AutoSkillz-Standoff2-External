//
// Created by wergity on 08.04.2025.
//

#ifndef VARS_H
#define VARS_H

#define MAX_PLAYERS_IN_GAME  10

struct vector2_t
{
  float x;
  float y;
};

struct preferences_t
{
  float screen_width;
  float screen_height;

  bool  aimbot;
  bool  silent;
  int   smoothness;
  bool  show_fov;
  int   fov_width;
  int   fov_height;
  bool  visibility_checks;
  bool  ignore_off_screen;
  int   target_selection;
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

namespace vars
{
  extern preferences_t preferences;

  extern bool friendly_esp;
  extern bool bounding_box;
  extern bool line;
  extern bool distance;
  extern bool health_bar;
  extern int  ally_color;
  extern int  enemy_color;
}

#endif