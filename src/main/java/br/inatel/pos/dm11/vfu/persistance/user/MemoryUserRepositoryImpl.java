package br.inatel.pos.dm11.vfu.persistance.user;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MemoryUserRepositoryImpl implements UserRepository{

    private final Map<String, User> db = new HashMap<>();

    @Override
    public List<User> getAll() {
        return db.values().stream().toList();
    }

    @Override
    public Optional<User> getById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return db.values().stream().filter(user -> user.email().equals(email)).findFirst();
    }

    @Override
    public User save(User user) {
        return db.put(user.id(), user);
    }

    @Override
    public void delete(String id) {
        db.values().removeIf(user -> user.id().equals(id));
    }
}
