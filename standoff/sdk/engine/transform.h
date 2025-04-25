//
// Created by wergity on 07.04.2025.
//

#ifndef TRANSFORM_H
#define TRANSFORM_H

#include "sdk/sdk.h"

struct transform_t
{
  PADDING(void*,  matrix, 0x0, 0x38);
  int             index;
};

struct transform_access_t
{
  PADDING(void*, list, 0x0, 0x18);
  void*          indices;
};

struct transform_matrix_t
{
  vector4_t     position;
  quaternion_t  rotation;
  vector4_t     scale;
};

namespace transform
{
  vector3_t position(std::uintptr_t pointer);
}

#endif //TRANSFORM_H
