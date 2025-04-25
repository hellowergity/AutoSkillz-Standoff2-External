//
// Created by wergity on 07.04.2025.
//

#include "camera.h"

#include <utils/logcat.h>

float camera::screen_width  = 0;
float camera::screen_height = 0;

bool camera::is_out_of_screen(const vector3_t position)
{
  return position.z < 0.98f;
}

vector3_t camera::world_to_screen(const std::uintptr_t pointer, const vector3_t position)
{
  const std::uintptr_t cached_ptr = memory::read(pointer + offsetof(unity_pointer_t, cached_ptr));
  if (!cached_ptr) {
    return {};
  }

  view_matrix_t matrix {};
  if (!memory::read(cached_ptr + offsetof(camera_t, view_matrix), &matrix, sizeof(matrix))) {
    return {};
  }

  const auto transform = vector3_t(matrix.m14, matrix.m24, matrix.m34);
  const auto right = vector3_t(matrix.m11, matrix.m21, matrix.m31);
  const auto up = vector3_t(matrix.m12, matrix.m22, matrix.m32);

  const float y = up.dot(position) + matrix.m42;
  const float x = right.dot(position) + matrix.m41;
  const float w = transform.dot(position) + matrix.m44;

  const vector3_t result = {
    screen_width / 2.f * (1.f + x / w),
    screen_height / 2.f * (1.f - y / w),
    w
  };

  return result;
}