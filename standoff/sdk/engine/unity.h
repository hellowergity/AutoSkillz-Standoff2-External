//
// Created by wergity on 07.04.2025.
//

#pragma once

struct unity_pointer_t
{
  std::uintptr_t padding[2];
  std::uintptr_t cached_ptr;
};

struct vector3_t
{
  float x;
  float y;
  float z;

  vector3_t() = default;

  vector3_t(const float x, const float y, const float z)
  {
    this->x = x;
    this->y = y;
    this->z = z;
  }

  [[nodiscard]] float distance(const vector3_t vector) const
  {
    const vector3_t delta = *this - vector;
    return sqrtf(delta.x * delta.x + delta.y * delta.y + delta.z * delta.z);
  }

  [[nodiscard]] float dot(const vector3_t vector) const
  {
    return x * vector.x + y * vector.y + z * vector.z;
  }

  [[nodiscard]] bool is_zero() const
  {
    return x == 0 && y == 0 && z == 0;
  }

  vector3_t operator-(const vector3_t& vector) const
  {
    return {x - vector.x, y - vector.y, z - vector.z};
  }
};

struct vector4_t
{
  float x;
  float y;
  float z;
  float w;
};

struct quaternion_t
{
  float x;
  float y;
  float z;
  float w;
};