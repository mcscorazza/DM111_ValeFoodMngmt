package br.inatel.pos.dm11.vfu.api.restaurant.service;

import br.inatel.pos.dm11.vfu.api.core.ApiException;
import br.inatel.pos.dm11.vfu.api.core.AppErrorCode;
import br.inatel.pos.dm11.vfu.api.restaurant.ProductRequest;
import br.inatel.pos.dm11.vfu.api.restaurant.ProductResponse;
import br.inatel.pos.dm11.vfu.api.restaurant.RestaurantRequest;
import br.inatel.pos.dm11.vfu.api.restaurant.RestaurantResponse;
import br.inatel.pos.dm11.vfu.api.user.UserRequest;
import br.inatel.pos.dm11.vfu.api.user.UserResponse;
import br.inatel.pos.dm11.vfu.persistance.restaurant.Product;
import br.inatel.pos.dm11.vfu.persistance.restaurant.Restaurant;
import br.inatel.pos.dm11.vfu.persistance.restaurant.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);
    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }
    //#############################################
    //##                PUBLIC                   ##
    //#############################################
    public List<RestaurantResponse> searchRestaurants() {
        var restaurants = repository.getAll();
        return restaurants.stream().map(this::buildRestaurantResponse).toList();
    }

    public RestaurantResponse createRestaurant(RestaurantRequest request) throws ApiException {
        var restaurant = buildRestaurant(request);
        repository.save(restaurant);
        log.info("Restaurant was successfully created. Id: {}", restaurant.id());
        return buildRestaurantResponse(restaurant);
    }

    //#############################################
    //##                PRIVATE                  ##
    //#############################################
    private Restaurant buildRestaurant(RestaurantRequest req) {
        var products = req.products().stream()
                .map(this::buildProduct).toList();
        var restaurantId = UUID.randomUUID().toString();
        return new Restaurant(restaurantId,
                req.name(),
                req.address(),
                req.userId(),
                req.categories(),
                products);
    }

    private Product buildProduct(ProductRequest req) {
        var productId = UUID.randomUUID().toString();
        return new Product( productId,
                req.name(),
                req.description(),
                req.category(),
                req.price()
        );
    }

    private RestaurantResponse buildRestaurantResponse(Restaurant restaurant) {
        var products = restaurant.products().stream()
                .map(this::buildProductResponse).toList();

        return new RestaurantResponse(
                restaurant.id(),
                restaurant.name(),
                restaurant.address(),
                restaurant.userId(),
                restaurant.categories(),
                products
        );
    }

    private ProductResponse buildProductResponse(Product product) {
        return new ProductResponse(
                product.id(),
                product.name(),
                product.description(),
                product.category(),
                product.price()
        );
    }

}
