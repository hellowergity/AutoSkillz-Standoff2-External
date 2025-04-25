//
// Created by wergity on 07.04.2025.
//

#pragma once

#pragma pack(push, 1)
struct string_t
{
  void* klass;
  void* monitor;
  int   length;
  void* chars;
};
#pragma pack(pop)

struct il2cpp_array_t
{
  void* klass;
  void* monitor;
  void* bounds;
  int   capacity;
  void* items;
};

struct il2cpp_dictionary_t
{
  struct entry_t {
    int   hash;
    int   next;
    void* key;
    void* value;
  };

  void* klass;
  void* monitor;
  void* buckets;
  void* entries;
  int   count;
};

struct il2cpp_type_t
{
  void*        data;
  unsigned int bits;
};

struct il2cpp_class_t
{
  void* image;
  void* gc_desc;
  void* name;
  void* namespaze;
  il2cpp_type_t byval_arg;
  il2cpp_type_t this_arg;
  void* element_class;
  void* cast_class;
  void* declaring_type;
  void* parent;
  void* generic_class;
  void* type_metadata_handle;
  void* interop_data;
  void* klass;
  void* fields;
  void* events;
  void* properties;
  void* methods;
  void* nested_types;
  void* implemented_interfaces;
  void* interface_offsets;
  void* static_fields;
};