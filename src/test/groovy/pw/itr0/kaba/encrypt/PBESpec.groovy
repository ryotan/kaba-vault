package pw.itr0.kaba.encrypt

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import javax.crypto.BadPaddingException
import java.nio.charset.StandardCharsets

/**
 * Specification class for {@link PBE}.
 *
 * @author ryotan
 */
class PBESpec extends Specification {
  @Rule
  TemporaryFolder temp

  def "デフォルトのsalt, iterationCountで暗号化したものは、デフォルトのsalt, iterationCountで復号できること"() {
    when:
    def encrypter = PBE.getEncrypter()
    def decrypter = PBE.getDecrypter()
    def raw = '暗号化したい文字列なのですよ〜☺'.getBytes(StandardCharsets.UTF_8)
    def password = 'password'.toCharArray()

    then:
    encrypter.encrypt(raw, password) != raw
    decrypter.decrypt(encrypter.encrypt(raw, password), password) == raw
  }

  def "指定したsalt, iterationCountで暗号化したものは、同じsalt, iterationCountで復号できること"() {
    when:
    def encrypter = PBE.getEncrypter('some salt'.getBytes(), 2)
    def decrypter = PBE.getDecrypter('some salt'.getBytes(), 2)
    def raw = '暗号化したい文字列なのですよ〜☺'.getBytes(StandardCharsets.UTF_8)
    def password = 'password'.toCharArray()

    then:
    encrypter.encrypt(raw, password) != raw
    decrypter.decrypt(encrypter.encrypt(raw, password), password) == raw
  }

  def "指定したsalt, iterationCountで暗号化したものは、異なるsalt, iterationCountで復号できないこと"() {
    given:
    def encrypter = PBE.getEncrypter('some salt'.getBytes(), 2)
    def decrypter = PBE.getDecrypter('some salt'.getBytes(), 3)
    def raw = '暗号化したい文字列なのですよ〜☺'.getBytes(StandardCharsets.UTF_8)
    def password = 'password'.toCharArray()

    when:
    encrypter.encrypt(raw, password) != raw
    decrypter.decrypt(encrypter.encrypt(raw, password), password) == raw

    then:
    thrown BadPaddingException
  }

  def "OutputStreamを暗号化し、InputStreamを復号できること"() {
    given:
    def encrypter = PBE.getEncrypter('some salt'.getBytes(), 2)
    def decrypter = PBE.getDecrypter('some salt'.getBytes(), 2)
    def password = 'password'.toCharArray()
    def baos = new ByteArrayOutputStream()
    def os = encrypter.wrap(baos, password)
    def raw = '暗号化したい文字列なのですよ〜☺'.getBytes(StandardCharsets.UTF_8)

    when:
    os.write(raw)
    os.close()
    def is = decrypter.wrap(new ByteArrayInputStream(baos.toByteArray()), password)
    def read = new byte[raw.length]
    is.read(read)
    is.close()

    then:
    read == raw
  }

  def "暗号化されたファイルを出力できること"() {
    given:
    def out = temp.newFile()
    def password = 'password'.toCharArray()
    def os = PBE.getEncrypter('some salt'.getBytes(), 2).wrap(new FileOutputStream(out), password)

    when:
    os.write("あんごうかするでぇ".getBytes(StandardCharsets.UTF_8))
    os.close()

    then:
    out.bytes == PBE.getEncrypter('some salt'.getBytes(), 2).encrypt("あんごうかするでぇ".getBytes(StandardCharsets.UTF_8), password)
  }

  def "暗号化されたファイル読み込んで復号できること"() {
    given:
    def out = temp.newFile()
    def password = 'password'.toCharArray()
    def os = PBE.getEncrypter('some salt'.getBytes(), 2).wrap(new FileOutputStream(out), password)
    os.write("あんごうかするでぇ".getBytes(StandardCharsets.UTF_8))
    os.close()

    when:
    def is = PBE.getDecrypter('some salt'.getBytes(), 2).wrap(new FileInputStream(out), password)

    then:
    is.newReader(StandardCharsets.UTF_8.toString()).text == "あんごうかするでぇ"
  }

  def "カバレッジツールがprivate constructorをignoreしてくれればいいのに。。。"() {
    def util = new PBE()

    expect:
    util instanceof PBE
  }
}
