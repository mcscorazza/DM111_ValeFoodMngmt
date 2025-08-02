package br.inatel.pos.dm11.vfu.api.user;

public record UserRequest(String name, String email, String password, String type) {
}
