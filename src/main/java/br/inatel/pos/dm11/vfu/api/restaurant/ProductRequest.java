package br.inatel.pos.dm11.vfu.api.restaurant;

public record ProductRequest(String name,
                             String description,
                             String category,
                             float price) {
}
