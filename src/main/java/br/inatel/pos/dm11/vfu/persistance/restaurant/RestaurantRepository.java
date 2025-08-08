package br.inatel.pos.dm11.vfu.persistance.restaurant;

import br.inatel.pos.dm11.vfu.persistance.ValeFoodRepository;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface RestaurantRepository extends ValeFoodRepository<Restaurant> {
    Optional<Restaurant> getByUserId(String userId) throws ExecutionException, InterruptedException;
}