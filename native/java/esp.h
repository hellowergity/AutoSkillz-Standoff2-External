//
// Created by wergity on 15.04.2025.
//

#ifndef ESP_H
#define ESP_H

#include <jni.h>
#include <memory>

#include "vars.h"

class esp
{
private:
  JNIEnv* env;
  jobject view;
  jobject canvas;

  [[nodiscard]] float width() const;

  [[nodiscard]] float height() const;

  void line(int color, float width, vector2_t start, vector2_t end) const;

  void box(int color, rect_t rect) const;

  void text(int color, vector2_t position, const std::string& text) const;

  void health_bar(int hp, rect_t rect) const;

  void draw() const;

public:
  esp()  = default;
  ~esp() = default;

  player_list_t player_list;

  static void signal(JNIEnv* env, jclass clazz, jobject view, jobject canvas);
};

extern std::shared_ptr<esp> overlay;

#endif //ESP_H