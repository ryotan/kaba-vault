package pw.itr0.kaba.vault.keystore

import org.junit.ClassRule
import org.junit.rules.TemporaryFolder
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 *
 * @author Ryo Tanaka
 */
@Unroll
class JCEKSKeyStoreVaultSpec extends Specification {
  @ClassRule
  @Shared
  TemporaryFolder tmp = new TemporaryFolder();

  def "保存"() {
    given:
    def vault = new JCEKSKeyStoreVault(new File("${tmp.getRoot()}/store-test.ks").toPath(), "password".toCharArray())

    when:
    def list = vault.list()
    vault.store('alias name', new KeyStoreEntry())

    then:
    list.isEmpty()
  }

  def "保存２"() {
  }
}
