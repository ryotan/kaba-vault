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
    'some password'              | 'SEALED:V1:dOWFWhYR+T51iFvpoJVuEg=='
    ''                           | 'SEALED:V1:sJuog59Tn4NavQhPaWuLJg=='
    '1'                          | 'SEALED:V1:hLop0nXnx/RKEQXKjNVFCQ=='
    'シール'                        | 'SEALED:V1:GxlNbVVii3RZgzjgbvaZ4g=='
    'sealed:A'                   | 'SEALED:V1:FbxS2THauf8Kk1fzWbrk1Q=='
    '123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789000000000' +
        '0012345678901234576890' |
        'SEALED:V1:Rkce7vJsTGPluTk+cRi7iJnFCC0hFalcUzYewJGkKS0xKcKgO7vKkUTLI/PEQLEjpvTzzYPaUGmqrpA1jmDPH+BMOXhQ64TULiI0xGM+VWA+Ph/t8O8/fiYtm8oyzhXIxfNawUNa/z3x4x5vnhxNP3wxn7lo32yRup2U7lgJedwvOyKwaxUXBdUizIFuUEjV'
  }

  def '暗号化されたパスワードが、正しく復号できること'(String password, String sealed) {
    expect:
    PasswordUtil.unseal(sealed) == password.getBytes()

    where:
    password                     | sealed
    'some password'              | 'SEALED:V1:dOWFWhYR+T51iFvpoJVuEg=='
    ''                           | 'SEALED:V1:sJuog59Tn4NavQhPaWuLJg=='
    '1'                          | 'SEALED:V1:hLop0nXnx/RKEQXKjNVFCQ=='
    'シール'                        | 'SEALED:V1:GxlNbVVii3RZgzjgbvaZ4g=='
    'sealed:A'                   | 'SEALED:V1:FbxS2THauf8Kk1fzWbrk1Q=='
    '123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789000000000' +
        '0012345678901234576890' |
        'SEALED:V1:Rkce7vJsTGPluTk+cRi7iJnFCC0hFalcUzYewJGkKS0xKcKgO7vKkUTLI/PEQLEjpvTzzYPaUGmqrpA1jmDPH+BMOXhQ64TULiI0xGM+VWA+Ph/t8O8/fiYtm8oyzhXIxfNawUNa/z3x4x5vnhxNP3wxn7lo32yRup2U7lgJedwvOyKwaxUXBdUizIFuUEjV'
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
