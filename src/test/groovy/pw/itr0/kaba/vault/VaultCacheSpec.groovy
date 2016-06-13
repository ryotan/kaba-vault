package pw.itr0.kaba.vault

import spock.lang.Shared
import spock.lang.Specification
/**
 * Specification class for {@link VaultCache}.
 *
 * @author ryotan
 */
class VaultCacheSpec extends Specification {

  @Shared
  def factory = { name ->
    Mock(Vault)
  }

  def "ひととおりのライフサイクルをテストする。"() {
    when: "cachedという名前でVaultをキャッシュした場合"
    VaultCache.registerIfAbsent("cache", factory)

    then: "cachedという名前でキャッシュからインスタンスが取得できること。"
    def cached = VaultCache.get("cache")
    cached != null

    and: "factoryを渡しても、factoryから新しいインスタンスが生成されないこと。"
    cached == VaultCache.get("cache", factory)

    and: "存在しない名前ではインスタンスが取得できないこと。"
    VaultCache.get("not exist") == null

    when: "forceRegisterで新しいインスタンスを強制的に登録した場合"
    VaultCache.forceRegister("cache", factory)

    then: "もともと登録されていたインスタンスが上書きされること。"
    cached != VaultCache.get("cache")

    and: "getに存在しない名前とfactoryを渡すと、factoryから新しいインスタンスを生成して返却すること。"
    VaultCache.get("not exist", factory) != null

    when: "キャッシュに存在しないときにforceRegisterした場合"
    VaultCache.forceRegister("cache2", factory)

    then: "問題なくキャッシュされて新しいインスタンスが取得できること。"
    VaultCache.get("cache2") != null
  }

  def "カバレッジツールがprivate constructorをignoreしてくれればいいのに。。。"() {
    def util = new VaultCache()

    expect:
    util instanceof VaultCache
  }
}
