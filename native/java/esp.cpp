//
// Created by wergity on 15.04.2025.
//

#include "esp.h"

#include <format>
#include <utils/logcat.h>

auto overlay = std::make_shared<esp>();

float esp::width() const
{
  jclass klass = env->GetObjectClass(canvas);
  jmethodID method = env->GetMethodID(klass, "getWidth", "()I");

  return static_cast<float>(env->CallIntMethod(canvas, method));
}

float esp::height() const
{
  jclass klass = env->GetObjectClass(canvas);
  jmethodID method = env->GetMethodID(klass, "getHeight", "()I");

  return static_cast<float>(env->CallIntMethod(canvas, method));
}

void esp::line(int color, float width, vector2_t start, vector2_t end) const
{
  jclass klass = env->GetObjectClass(view);
  jmethodID method = env->GetStaticMethodID(klass, "line", "(Landroid/graphics/Canvas;IFFFFF)V");
  env->CallStaticVoidMethod(klass, method, canvas, color, width, start.x, start.y, end.x, end.y);
}

void esp::box(int color, rect_t rect) const
{
  rect.x -= rect.w / 2;

  auto tl = vector2_t(rect.x + rect.w,  rect.y + rect.h);
  auto tr = vector2_t(rect.x,  rect.y + rect.h);
  auto bl = vector2_t(rect.x + rect.w,  rect.y);
  auto br = vector2_t(rect.x,  rect.y);

  line(color, 1, tl, tr);
  line(color, 1, tr, br);
  line(color, 1, br, bl);
  line(color, 1, bl, tl);
}

void esp::text(int color, vector2_t position, const std::string& text) const
{
  jclass klass = env->GetObjectClass(view);
  jmethodID method = env->GetStaticMethodID(klass, "text", "(Landroid/graphics/Canvas;ILjava/lang/String;FF)V");

  jstring data = env->NewStringUTF(text.c_str());
  env->CallStaticVoidMethod(klass, method, canvas, color, data, position.x, position.y);
}

void esp::health_bar(int hp, rect_t rect) const
{
  float width  = 1.5f;
  float height = 4.5f;
  int color    = 4; // GREEN

  if (hp <= 66) {
    color = 3; // YELLOW
  }

  if (hp <= 33) {
    color = 2; // RED
  }

  rect.x -= rect.w / 2;
  rect.y += rect.h - height;

  auto start = vector2_t(rect.x + rect.w, rect.y);
  auto end   = vector2_t(rect.x, rect.y);

  line(1, height, start, end); // black

  end.x = start.x - static_cast<float>(hp) * rect.w / 100 - width;
  start.x += width;

  line(color, height / 2, start, end);
}

void esp::draw() const
{
  for (int i = 0; i < player_list.count; i++) {
    const player_t* player = &player_list.array[i];

    if (!vars::friendly_esp && !player->enemy) {
      continue;
    }

    rect_t rect = player->location;
    float width = vars::preferences.screen_width;
    float height = vars::preferences.screen_height;
    int color = player->enemy ? vars::enemy_color : vars::ally_color;

    if (vars::bounding_box) {
      overlay->box(color, rect);
    }

    if (vars::line) {
      vector2_t start(width / 2.f, 0);
      vector2_t end(rect.x, rect.y);

      if (player->enemy) {
        end.y += rect.h;
      } else {
        start.y += height;
      }

      overlay->line(color, 1, start, end);
    }

    if (vars::distance) {
      std::string text = std::format("{:.1f} M", player->distance);
      overlay->text(color, vector2_t(rect.x, rect.y), text);
    }

    if (vars::health_bar) {
      overlay->health_bar(player->health, rect);
    }
  }
}

void esp::signal(JNIEnv* env, [[maybe_unused]] jclass clazz, jobject view, jobject canvas)
{
  overlay->env    = env;
  overlay->view   = view;
  overlay->canvas = canvas;

  vars::preferences.screen_width  = overlay->width();
  vars::preferences.screen_height = overlay->height();

  overlay->draw();
}