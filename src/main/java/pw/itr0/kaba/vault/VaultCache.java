package pw.itr0.kaba.vault;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Simple caching utility for {@link Vault} classes.
 *
 * @author ryotan
 * @since 1.0.0
 */
public final class VaultCache {

    /**
     * {@link ConcurrentHashMap} for cache.
     */
    private static final ConcurrentHashMap<String, Vault> cache = new ConcurrentHashMap<>();

    /**
     * Hidden constructor for utility class.
     */
    private VaultCache() {
        // nop
    }

    /**
     * Add {@link Vault} to cache, even if {@code name} is already registered.
     *
     * @param name  {@link Vault} name in cache
     * @param factory {@link Vault} supplier {@link Function} which accepts {@code name} as an argument
     */
    public static void forceRegister(String name, Function<String, Vault> factory) {
        cache.compute(name, (n, v) -> factory.apply(n));
    }

    /**
     * Add {@link Vault} to cache if {@code name} is not registered.
     *
     * @param name    {@link Vault} name in cache
     * @param factory {@link Vault} supplier {@link Function} which accepts {@code name} as an argument
     */
    public static void registerIfAbsent(String name, Function<String, Vault> factory) {
        cache.computeIfAbsent(name, factory);
    }

    /**
     * Get {@link Vault} from cache. If {@code name} is not registered, add result of {@code factory} to cache and return it.
     *
     * @param name    {@link Vault} name in cache
     * @param factory {@link Vault} supplier {@link Function} which accepts {@code name} as an argument
     * @return Cached {@link Vault}
     */
    public static Vault get(String name, Function<String, Vault> factory) {
        return cache.computeIfAbsent(name, factory);
    }

    /**
     * Get {@link Vault} from cache. If {@code name} is not registered, returns {@code null}.
     *
     * @param name {@link Vault} name in cache
     * @return Cached {@link Vault} or {@code null} if {@code name} is not in cache
     */
    public static Vault get(String name) {
        return cache.get(name);
    }
}
