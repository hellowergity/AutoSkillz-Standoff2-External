//
// Created by wergity on 08.04.2025.
//

#ifndef MENU_H
#define MENU_H

#include <jni.h>

namespace menu
{
  enum tab_t : jint
  {
    tab_aim,
    tab_visuals
  };

  enum aim_t : jint
  {
    aimbot,
    silent,
    smoothness,
    show_fov,
    fov_width,
    fov_height,
    visibility_checks,
    ignore_off_screen,
    target_selection
  };

  enum visuals_t : jint
  {
    friendly_esp,
    bounding_box,
    line,
    distance,
    health_bar,
    ally_color,
    enemy_color
  };

  void aim(aim_t index, int value);

  void visuals(visuals_t index, int value);

  void signal(JNIEnv* env, [[maybe_unused]] jclass clazz, tab_t tab, int index, int value);
}

#endif //MENU_H