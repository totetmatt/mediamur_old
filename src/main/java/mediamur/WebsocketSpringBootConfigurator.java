package mediamur;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
/**
 * 
 * @author Totetmatt
 * Utility class to make Web socket working on spring boot
 */
@Configuration
public class WebsocketSpringBootConfigurator {

    @Bean
    public WebsocketEndpoint echoEndpoint() {
        return new WebsocketEndpoint();
    }

    @Bean
    public ServletContextAware endpointExporterInitializer(final ApplicationContext applicationContext) {
        return new ServletContextAware() {

            public void setServletContext(ServletContext servletContext) {
                ServerEndpointExporter serverEndpointExporter = new ServerEndpointExporter();
                serverEndpointExporter.setApplicationContext(applicationContext);
                try {
                    serverEndpointExporter.afterPropertiesSet();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }               
            }           
        };
    }
}
