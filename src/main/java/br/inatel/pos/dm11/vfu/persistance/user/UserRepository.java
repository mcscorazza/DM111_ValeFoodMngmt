package br.inatel.pos.dm11.vfu.persistance.user;

import java.util.List;
import java.util.Optional;


public interface UserRepository {
    List<User> getAll();
    Optional<User> getById(String id);
    Optional<User> getByEmail(String email);
    User save(User user);
    void delete(String id);
}
