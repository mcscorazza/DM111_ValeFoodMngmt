package br.inatel.pos.dm11.vfu.persistance.restaurant;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MemoryRestaurantRepositoryImpl implements RestaurantRepository {

    private final Map<String, Restaurant> db = new HashMap<>();

    @Override
    public List<Restaurant> getAll() {
        return db.values().stream().toList();
    }

    @Override
    public Optional<Restaurant> getById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public Optional<Restaurant> getByUserId(String userId) {
        return db.values().stream().filter(restaurant -> restaurant.userId().equals(userId)).findFirst();
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return db.put(restaurant.id(), restaurant);
    }

    @Override
    public void delete(String id) {
        db.values().removeIf(restaurant -> restaurant.id().equals(id));
    }
}
