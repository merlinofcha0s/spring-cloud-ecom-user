package fr.plb.ecom_user.configuration;

import org.springframework.cloud.gateway.server.mvc.filter.AfterFilterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.time.Duration;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.stripPrefix;
import static org.springframework.cloud.gateway.server.mvc.filter.Bucket4jFilterFunctions.rateLimit;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayConfiguration {

    @Bean
    public RouterFunction<ServerResponse> productRoute() {
        return route("ecom_product_route")
                .route(path("/services/ecom-product/**"), http())
                .filter(lb("ecom-product"))
                .filter(rateLimit(c -> c.setCapacity(2)
                        .setPeriod(Duration.ofSeconds(20))
                        .setKeyResolver(request -> request.servletRequest().getUserPrincipal().getName())))
                .before(stripPrefix(2))
                .after(AfterFilterFunctions.addResponseHeader("X-Powered-by", "PLB"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderRoute() {
        return route("ecom_order_route")
                .route(path("/services/ecom-order/**"), http())
                .filter(lb("ecom-order"))
                .before(stripPrefix(2))
                .after(AfterFilterFunctions.addResponseHeader("X-Powered-by", "PLB"))
                .build();
    }
}
