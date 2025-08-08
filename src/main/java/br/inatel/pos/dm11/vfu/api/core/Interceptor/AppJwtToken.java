package br.inatel.pos.dm11.vfu.api.core.Interceptor;

public record AppJwtToken(String issuer,
                          String subject,
                          String role,
                          String method,
                          String uri) {
}
