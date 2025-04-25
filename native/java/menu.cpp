//
// Created by wergity on 08.04.2025.
//

#include "menu.h"

#include "vars.h"

#define SET_PREF(name) case name: \
  vars::preferences.name = value; \
  break

#define SET_VAR(name) case name: \
  vars::name = value;            \
  break

void menu::aim(const aim_t index, const int value)
{
  switch (index) {
    default: break;
    SET_PREF(aimbot);
    SET_PREF(silent);
    SET_PREF(smoothness);
    SET_PREF(show_fov);
    SET_PREF(fov_width);
    SET_PREF(fov_height);
    SET_PREF(visibility_checks);
    SET_PREF(ignore_off_screen);
    SET_PREF(target_selection);
  }
}

void menu::visuals(const visuals_t index, const int value)
{
  switch (index) {
    default: break;
    SET_VAR(friendly_esp);
    SET_VAR(bounding_box);
    SET_VAR(line);
    SET_VAR(distance);
    SET_VAR(health_bar);
    SET_VAR(ally_color);
    SET_VAR(enemy_color);
  }
}

void menu::signal([[maybe_unused]] JNIEnv* env, [[maybe_unused]] jclass clazz, const tab_t tab, int index, const int value)
{
  switch (tab) {
    case tab_aim:
      aim(static_cast<aim_t>(index), value);
      break;

    case tab_visuals:
      visuals(static_cast<visuals_t>(index), value);
      break;

    default:
      break;
  }
}