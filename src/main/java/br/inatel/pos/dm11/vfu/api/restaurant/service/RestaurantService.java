package br.inatel.pos.dm11.vfu.api.restaurant.service;

import br.inatel.pos.dm11.vfu.api.core.ApiException;
import br.inatel.pos.dm11.vfu.api.core.AppErrorCode;
import br.inatel.pos.dm11.vfu.api.restaurant.ProductRequest;
import br.inatel.pos.dm11.vfu.api.restaurant.ProductResponse;
import br.inatel.pos.dm11.vfu.api.restaurant.RestaurantRequest;
import br.inatel.pos.dm11.vfu.api.restaurant.RestaurantResponse;
import br.inatel.pos.dm11.vfu.persistance.restaurant.Product;
import br.inatel.pos.dm11.vfu.persistance.restaurant.Restaurant;
import br.inatel.pos.dm11.vfu.persistance.restaurant.RestaurantRepository;
import br.inatel.pos.dm11.vfu.persistance.user.User;
import br.inatel.pos.dm11.vfu.persistance.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository repository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }


    //#############################################
    //##                PUBLIC                   ##
    //#############################################
    public List<RestaurantResponse> searchRestaurants() {
        var restaurants = restaurantRepository.getAll();
        return restaurants.stream().map(this::buildRestaurantResponse).toList();
    }

    public RestaurantResponse searchRestaurant(String id) throws ApiException {
        return restaurantRepository.getById(id).map(this::buildRestaurantResponse)
                .orElseThrow(()->{
                    log.warn("Restaurant was not found. Id: {}", id);
                    return new ApiException((AppErrorCode.RESTAURANT_NOT_FOUND));
                });
    }

    public RestaurantResponse createRestaurant(RestaurantRequest req) throws ApiException {

        validateRestaurantUpdate(req);

        var restaurant = buildRestaurant(req);
        restaurantRepository.save(restaurant);
        log.info("Restaurant was successfully created. Id: {}", restaurant.id());
        return buildRestaurantResponse(restaurant);
    }

    public RestaurantResponse updateRestaurant(RestaurantRequest req, String id) throws ApiException {
        // check restaurant by id exist
        var restaurantOpt = restaurantRepository.getById(id);

        if (restaurantOpt.isEmpty()){
            log.warn("Restaurant was not found! Id: {}", id);
            throw  new ApiException(AppErrorCode.RESTAURANT_NOT_FOUND);
        } else {

            validateRestaurantUpdate(req);

            var UpdRestaurant = buildRestaurant(req, id);
            restaurantRepository.save(UpdRestaurant);
            log.info("Restaurant was successfully Updated! Id: {}", id);
            return buildRestaurantResponse(UpdRestaurant);
        }
    }

    public void removeRestaurant(String id) {
        restaurantRepository.delete(id);
        log.info("Restaurant was successfully deleted! Id: {}", id);
    }

    //#############################################
    //##                PRIVATE                  ##
    //#############################################
    private Restaurant buildRestaurant(RestaurantRequest req) {
        var restaurantId = UUID.randomUUID().toString();
        return buildRestaurant(req, restaurantId);
    }

    private Restaurant buildRestaurant(RestaurantRequest req, String restaurantId) {
        var products = req.products().stream()
                .map(this::buildProduct).toList();
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
    private void validateRestaurantUpdate(RestaurantRequest req) throws ApiException {
        var userOpt = userRepository.getById(req.userId());
        if (userOpt.isEmpty()){
            log.warn("User was not found! Id: {}", req.userId());
            throw  new ApiException(AppErrorCode.USER_NOT_FOUND);
        } else {
            var user = userOpt.get();
            if(User.UserType.RESTAURANT.equals(user.type())) {
                log.info("User provided is not valid for this operation. User Id: {}", req.userId());
                throw new ApiException(AppErrorCode.INVALID_USER_TYPE);
            }
        }
    }
}
