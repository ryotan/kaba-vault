package pw.itr0.kaba.encrypt

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Specification class for {@link PasswordUtil}.
 *
 * @author ryotan
 */
@Unroll
class PasswordUtilSpec extends Specification {

  def 'パスワードが暗号化され、容易には復号できなくなること'(String password, String sealed) {
    expect:
    PasswordUtil.seal(password.getBytes()) == sealed

    where:
    password                     | sealed
    'some password'              | 'SEALED:V1:Zk+uOrXyuvtxctg2aTFbbw=='
    ''                           | 'SEALED:V1:qgi1AOvFXQRJ6CDEiLa6JA=='
    '1'                          | 'SEALED:V1:vI568WGuDHH8JMMxr9j0Sg=='
    'シール'                        | 'SEALED:V1:I4OK+IsNK12FXHDnKSAukA=='
    'sealed:A'                   | 'SEALED:V1:R86Gn54SP9hOFyvu9OGz0A=='
    '123456789123456789123456789' +
        '123456789123456789123456789' +
        '123456789123456789123456789' +
        '123456789123456789000000000' +
        '0012345678901234576890' |
        'SEALED:V1:5B7cwi4kp4vGEEI97sHTdA7VGkCGgiUwhNw0KMxb3Xi+/Pm1DCL57BB0uKxlWV8NNuxNi9lAFqqwkhcrl0+sdCpweGi3iXOK0+2m3NwE2uOGeab5QIRhxiJJpmWEwnouml2DhANoOPiAEPtOaQ97vmdCUF86YhdC2n3c/4zlowLhC8NFeTeaRzaMLDdwpB1K'
  }

  def '暗号化されたパスワードが、正しく復号できること'(String password, String sealed) {
    expect:
    PasswordUtil.unseal(sealed) == password.getBytes()

    where:
    password                     | sealed
    'some password'              | 'SEALED:V1:Zk+uOrXyuvtxctg2aTFbbw=='
    ''                           | 'SEALED:V1:qgi1AOvFXQRJ6CDEiLa6JA=='
    '1'                          | 'SEALED:V1:vI568WGuDHH8JMMxr9j0Sg=='
    'シール'                        | 'SEALED:V1:I4OK+IsNK12FXHDnKSAukA=='
    'sealed:A'                   | 'SEALED:V1:R86Gn54SP9hOFyvu9OGz0A=='
    '123456789123456789123456789' +
        '123456789123456789123456789' +
        '123456789123456789123456789' +
        '123456789123456789000000000' +
        '0012345678901234576890' |
        'SEALED:V1:5B7cwi4kp4vGEEI97sHTdA7VGkCGgiUwhNw0KMxb3Xi+/Pm1DCL57BB0uKxlWV8NNuxNi9lAFqqwkhcrl0+sdCpweGi3iXOK0+2m3NwE2uOGeab5QIRhxiJJpmWEwnouml2DhANoOPiAEPtOaQ97vmdCUF86YhdC2n3c/4zlowLhC8NFeTeaRzaMLDdwpB1K'
    '12345'                      | '12345'
    ''                           | ''
  }

  def "パスワードを暗号化・復号できること"(String password) {
    expect:
    new String(PasswordUtil.unseal(PasswordUtil.seal(password.getBytes()))) == password

    where:
    password                                                                                                                             | _
    'some password'                                                                                                                      | _
    ''                                                                                                                                   | _
    '1'                                                                                                                                  | _
    'シール'                                                                                                                                | _
    'sealed:A'                                                                                                                           | _
    '1234567891234567891234567891234567891234567891234567891234567891234567891234567891234567891234567890000000000012345678901234576890' | _
  }

  def "カバレッジツールがprivate constructorをignoreしてくれればいいのに。。。"() {
    def util = new PasswordUtil()

    expect:
    util instanceof PasswordUtil
  }
}
