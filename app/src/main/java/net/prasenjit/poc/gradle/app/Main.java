package net.prasenjit.poc.gradle.app;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;

public class Main {
    public static void main(String[] args) throws ServletException {
        DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(MyServlet.class.getClassLoader())
                .setContextPath("/")
                .setDeploymentName("test.war")
                .addFilter(Servlets.filter("LoggingFilter", LoggingFilter.class)
                        .addInitParam("dumpRequest", "true")
                        .addInitParam("dumpResponse", "true")
                ).addFilterServletNameMapping("LoggingFilter", "MessageServlet", DispatcherType.REQUEST)
                .addServlets(
                        Servlets.servlet("MessageServlet", MyServlet.class)
                                .addInitParam("backendUri", "http://localhost:8081")
                                .addMapping("/*")
                );

        DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
        manager.deploy();
        PathHandler path = Handlers.path(Handlers.redirect("/myapp"))
                .addPrefixPath("/myapp", manager.start());

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(manager.start())
                .build();
        server.start();
    }
}
