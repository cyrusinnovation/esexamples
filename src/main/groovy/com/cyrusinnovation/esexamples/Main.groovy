package com.cyrusinnovation.esexamples

import org.eclipse.jetty.server.Connector
import org.eclipse.jetty.server.ForwardedRequestCustomizer
import org.eclipse.jetty.server.HttpConfiguration
import org.eclipse.jetty.server.HttpConnectionFactory
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.server.session.SessionHandler
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.webapp.Configuration
import org.eclipse.jetty.webapp.WebAppContext

class Main {
    public static void main(String[] args) {
        def port = System.getProperty("PORT", "8085").toInteger()
        def server = new Server()
        setupConnector(server, port)
        def handler = getServletContextHandler(server, Main.getClass().getResource("/esexamples/").path)
        server.setHandler(handler)
        server.start()
        server.join()
    }

    private static void setupConnector(Server server, int port) {
        def httpConfig = new HttpConfiguration()
        httpConfig.addCustomizer(new ForwardedRequestCustomizer())
        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(httpConfig))
        http.setPort(port)
        server.setConnectors([http] as Connector[])
    }
    private static ServletContextHandler getServletContextHandler(Server server, String resourceBase) {
        def context = new WebAppContext();
        context.setResourceBase(resourceBase);
        Configuration.ClassList classlist = Configuration.ClassList.setServerDefault(server);
        classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration",
                "org.eclipse.jetty.plus.webapp.PlusConfiguration");
        classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
                "org.eclipse.jetty.annotations.AnnotationConfiguration");
        context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",".*/classes/.*");
        return context;
    }
}
