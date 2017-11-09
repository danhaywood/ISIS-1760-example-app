package domainapp.webapp;

import java.net.URL;
import java.sql.SQLException;

import com.google.common.io.Resources;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.junit.Test;

public class IgniteTest {

    @Test
    public void can_bootstrap() throws SQLException, ClassNotFoundException {

        final URL resource = Resources.getResource(getClass(), "example-ignite.xml");
        try (Ignite ignite = Ignition.start(resource)) {
            // Put values in cache.
            IgniteCache<Integer, String> cache = ignite.getOrCreateCache("myCache");
            cache.put(1, "Hello");
            cache.put(2, "World!");
            // Get values from cache
            // Broadcast 'Hello World' on all the nodes in the cluster.
            ignite.compute().broadcast(()->System.out.println(cache.get(1) + " " + cache.get(2)));
        }
    }
}
