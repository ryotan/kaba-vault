package pw.itr0.kaba.vault;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public final class VaultCache {
    private static final ConcurrentHashMap<String, Vault> cache = new ConcurrentHashMap<>();

    public static void register(String name, Function<String, Vault> factory) {
        cache.computeIfAbsent(name, factory);
    }

    public static Vault computeIfAbsent(String name, Function<String, Vault> factory) {
        return cache.computeIfAbsent(name, factory);
    }

    public static Vault get(String name) {
        return cache.get(name);
    }


    private VaultCache() {
    }
}
