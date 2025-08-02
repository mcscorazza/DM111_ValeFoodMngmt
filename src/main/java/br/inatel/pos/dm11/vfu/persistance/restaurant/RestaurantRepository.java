package br.inatel.pos.dm11.vfu.persistance.restaurant;

import br.inatel.pos.dm11.vfu.persistance.user.User;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {
    List<Restaurant> getAll();
    Optional<Restaurant> getById(String id);
    Optional<Restaurant> getByUserId(String userId);
    Restaurant save(Restaurant restaurant);
    void delete(String id);
}
