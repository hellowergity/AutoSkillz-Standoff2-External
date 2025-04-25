//
// Created by wergity on 07.04.2025.
//

#include "transform.h"

vector3_t transform::position(const std::uintptr_t pointer)
{
  const std::uintptr_t cached_ptr = memory::read(pointer + offsetof(unity_pointer_t, cached_ptr));
  if (!cached_ptr) {
    return {};
  }

  const std::uintptr_t matrix = memory::read(cached_ptr + offsetof(transform_t, matrix));
  if (!matrix) {
    return {};
  }

  const std::uintptr_t list = memory::read(matrix + offsetof(transform_access_t, list));
  if (!list) {
    return {};
  }

  const std::uintptr_t indices = memory::read(matrix + offsetof(transform_access_t, indices));
  if (!indices) {
    return {};
  }

  int index;
  if (!memory::read(cached_ptr + offsetof(transform_t, index), &index, sizeof(index))) {
    return {};
  }

  vector3_t result{};
  if (!memory::read(list + index * sizeof(result), &result, sizeof(result))) { // list[index]
    return {};
  }

  while (true) {
    if ((index = memory::read<int>(indices + index * sizeof(index))) < 0) {
      break;
    }

    transform_matrix_t tm{};
    if (!memory::read(list + index * sizeof(tm), &tm, sizeof(tm))) { // list[index]
      break;
    }

    auto& [x, y, z, w] = tm.rotation;
    const vector3_t scale = {
      result.x * tm.scale.x,
      result.y * tm.scale.y,
      result.z * tm.scale.z
    };

    result.x  = scale.x + tm.position.x;
    result.x += scale.x * (y * y * -2.f - z * z *  2.f);
    result.x += scale.y * (w * z * -2.f - y * x * -2.f);
    result.x += scale.z * (z * x *  2.f - w * y * -2.f);

    result.y  = scale.y + tm.position.y;
    result.y += scale.x * (x * y *  2.f - w * z * -2.f);
    result.y += scale.y * (z * z * -2.f - x * x *  2.f);
    result.y += scale.z * (w * x * -2.f - z * y * -2.f);

    result.z  = scale.z + tm.position.z;
    result.z += scale.x * (w * y * -2.f - x * z * -2.f);
    result.z += scale.y * (y * z *  2.f - w * x * -2.f);
    result.z += scale.z * (x * x * -2.f - y * y *  2.f);
  }

  return result;
}