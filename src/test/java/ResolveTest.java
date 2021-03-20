import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.jar.JarFile;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResolveTest {
    @Test
    public void single() {
        EnviomentInitializer.init();
        MCKTResolver.Resolver resolver = new MCKTResolver.Resolver();
        resolver.addVersion(new int[] { 1, 4, 30 });

        assertArrayEquals(new int[] { 1, 4, 30 }, resolver.version);
    }

    @Test
    public void newer() {
        EnviomentInitializer.init();
        MCKTResolver.Resolver resolver = new MCKTResolver.Resolver();
        resolver.addVersion(new int[] { 1, 3, 30 });
        resolver.addVersion(new int[] { 1, 4, 30 });

        assertArrayEquals(new int[] { 1, 4, 30 }, resolver.version);
    }

    @Test
    public void missingJars() {
        EnviomentInitializer.init();
        MCKTResolver.Resolver resolver = new MCKTResolver.Resolver();

        resolver.collectKotlinInMod(new File("./build/libs/manifest-jar.jar"));

        assertEquals(Collections.emptySet(), resolver.candidateDownloads());
    }

    @Test
    public void detectShadowed() throws IOException {
        EnviomentInitializer.init();
        MCKTResolver.Resolver resolver = new MCKTResolver.Resolver();

        assertEquals(Constants.KOTLIN_VERSION_STR, resolver.detectKotlinVersion(new JarFile("./build/libs/kotlin-stdlib.jar")));
    }
}
