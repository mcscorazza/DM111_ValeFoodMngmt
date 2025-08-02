package br.inatel.pos.dm11.vfu.persistance.restaurant;

public record Product(String id,
                      String name,
                      String description,
                      String category,
                      float price) {
}
