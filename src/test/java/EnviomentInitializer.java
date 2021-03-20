import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.net.URL;

public class EnviomentInitializer {

    static {
        Launch.classLoader = new LaunchClassLoader(new URL[0]);
    }

    public static void init() {

    }
}
