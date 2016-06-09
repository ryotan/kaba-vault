package pw.itr0.kaba.vault.keystore

import org.junit.ClassRule
import org.junit.rules.TemporaryFolder
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Path

@Unroll
class JCEKSKeyStoreVaultSpec extends Specification {
  @ClassRule
  @Shared
  TemporaryFolder tmp = new TemporaryFolder();

  def "指定されたキーストアファイルが存在しない場合は、インスタンス生成時に空のキーストアファイルが新規作成されること。"() {
    given:
    def ksPath = newKeyStorePath()

    when:
    def vault = new SecretKeyStoreVault(ksPath, "password".chars)

    then:
    vault.list().isEmpty()
  }

  def "新規作成されたキーストアファイルに、共通鍵を保存できること。"() {
    given:
    def vault = new SecretKeyStoreVault(newKeyStorePath(), 'password for KeyStore'.chars)
    def secret = 'secret phrase to be encrypted.'.bytes

    when:
    vault.store('secret string to be stored without password.', secret)

    then:
    vault.retrieve('secret string to be stored without password.') == secret
  }

  private Path newKeyStorePath() {
    def ksPath = tmp.newFile().toPath()
    Files.deleteIfExists(ksPath)
    assert !Files.exists(ksPath)
    return ksPath
  }
}
