package com.progra.distribuida;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.consul.ConsulClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

@ApplicationScoped // Con esto hago q sea un componente CDI
public class TodoLifeCycle {

    @Inject
    @ConfigProperty(name = "consul.host", defaultValue = "8500")
    String consulHost;

    @Inject
    @ConfigProperty(name = "consul.port", defaultValue = "127.0.0.1")
    Integer consulPort;

    @Inject
    @ConfigProperty(name = "quarkus.http.port")
    Integer appPort;

    String serviceId;

    // Necesitamos escuchar 2 eventos: Inicializacion y cuando se detiene
    // Cuando arranca
    void init(@Observes StartupEvent event, Vertx vertx) throws Exception {
        try {
            System.out.println("Iniciando servicio de TODOO...");

            // Obtenemos host y puerto de la configuracion
            ConsulClientOptions options = new ConsulClientOptions()
                    .setHost(consulHost)
                    .setPort(consulPort);
            ConsulClient consulClient = ConsulClient.create(vertx, options);

            serviceId = UUID.randomUUID().toString();
            var ipAddress = InetAddress.getLocalHost();

            // Listo los tags que voy a usar
            var tags = List.of(
                    "traefik.enable=true",
                    "traefik.http.routers.app-rest.rule=PathPrefix(`/app-rest`)",
                    "traefik.http.routers.app-rest.middlewares=strip-prefix-rest",
                    "traefik.http.middlewares.strip-prefix-rest.stripprefix.prefixes=/app-rest"
            );

            var checkOptions = new CheckOptions()
//                .setHttp("http://127.0.0.1:8080/ping") // Esto estatico
                    .setHttp(String.format(("http://%s:%s/ping"), ipAddress.getHostAddress(), appPort))
                    .setInterval("10s")
                    .setDeregisterAfter("20s");

            ServiceOptions serviceOptions = new ServiceOptions()
                    .setName("app-rest")
                    .setId(serviceId)
                    .setAddress(ipAddress.getHostAddress())
                    .setPort(appPort)
                    .setTags(tags)
                    .setCheckOptions(checkOptions);

            consulClient.registerServiceAndAwait(serviceOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void stop(@Observes ShutdownEvent event, Vertx vertx) throws Exception {
        try {
            System.out.println("Deteniendo servicio de Todoo..");

            ConsulClientOptions options = new ConsulClientOptions()
                    .setHost(consulHost)
                    .setPort(consulPort);
            ConsulClient consulClient = ConsulClient.create(vertx, options);

            consulClient.deregisterServiceAndAwait(serviceId);
        } catch (Exception e) {
            System.out.println("Deteniendo servicio de todos...");

            ConsulClientOptions options = new ConsulClientOptions()
                    .setHost(consulHost)
                    .setPort(consulPort);
            ConsulClient consulClient = ConsulClient.create(vertx, options);

            consulClient.deregisterServiceAndAwait(serviceId);
        }
    }
}
