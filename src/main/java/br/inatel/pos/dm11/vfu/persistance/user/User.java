package br.inatel.pos.dm11.vfu.persistance.user;

public record User(String id, String name, String email, String password, UserType type) {
    public enum UserType { REGULAR, RESTAURANT }
}
