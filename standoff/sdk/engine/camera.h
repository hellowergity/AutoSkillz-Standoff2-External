//
// Created by wergity on 07.04.2025.
//

#ifndef CAMERA_H
#define CAMERA_H

#include "sdk/sdk.h"

struct view_matrix_t
{
  float m11, m12, m13, m14;
  float m21, m22, m23, m24;
  float m31, m32, m33, m34;
  float m41, m42, m43, m44;
};

struct camera_t
{
  PADDING(view_matrix_t, view_matrix, 0x0, 0x100);
};

namespace camera
{
  extern float screen_width;
  extern float screen_height;

  bool is_out_of_screen(vector3_t position);

  vector3_t world_to_screen(std::uintptr_t pointer, vector3_t position);
}

#endif //CAMERA_H
