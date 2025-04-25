#include <thread>

#include "features/esp.h"

#include "utils/client.h"
#include "utils/logcat.h"

void create_data_list(player_list_t& player_list)
{
  player_manager* manager = lazy_singleton::get_player_manager();
  if (!manager) {
    return;
  }

  player_controller* local_player = manager->controller();
  if (!local_player) {
    return;
  }

  const std::uintptr_t local_camera = local_player->camera();
  if (!local_camera) {
    return;
  }

  const team_t local_team = local_player->team();
  if (local_team == team_none) {
    return;
  }

  const vector3_t local_position = local_player->position();
  if (local_position.is_zero()) {
    return;
  }

  manager->for_each_players([&](player_controller* controller) -> void
  {
    player_t* player = &player_list.array[player_list.count];
    if ((player->health = controller->health()) < 1) {
      return;
    }

    const vector3_t world = controller->position();
    const vector3_t screen = camera::world_to_screen(local_camera, world);

    player->distance = local_position.distance(world);
    player->enemy = local_team != controller->team();

    if (esp::create(player, world, screen, local_camera)) {
      player_list.count++;
    }
  });
}

bool connect()
{
  const auto start = std::chrono::high_resolution_clock::now();

  client::status status;
  while ((status = client::initialize()) != client::status::created) {
    if (status == client::status::not_connected) {
      std::this_thread::sleep_for(std::chrono::seconds(1));
    }

    if (status == client::status::not_created) {
      return false;
    }
  }

  const auto delta = std::chrono::high_resolution_clock::now() - start;
  logcat::info("Connection took: %lld MS.", delta.count());
  return true;
}

int main()
{
  logcat::info("Hello cats from HelloKitty!");

  if (!connect()) {
    return -1;
  }

  while (client::recv(&preferences)) {
    if (!memory::initialize()) {
      continue;
    }

    camera::screen_width  = preferences.screen_width;
    camera::screen_height = preferences.screen_height;

    player_list_t player_list{};
    create_data_list(player_list);

    if (!client::send(&player_list, sizeof(player_list))) {
      break;
    }
  }

  logcat::warning("Daemon has finished work.");
  return 0;
}