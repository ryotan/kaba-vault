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
    'some password'              | 'SEALED:V1:1bB3EbMcYByendrEC4vraA=='
    ''                           | 'SEALED:V1:5+IVMWze0DvH/D4ASnPCfg=='
    '1'                          | 'SEALED:V1:/NSAZuGWFvggJPa5aQI27g=='
    'シール'                        | 'SEALED:V1:BFWYEOZKpYWP1l6v5RLX0A=='
    'sealed:A'                   | 'SEALED:V1:fSPdLoWYap3r1qpOa/Talg=='
    '123456789123456789123456789' +
        '123456789123456789123456789' +
        '123456789123456789123456789' +
        '123456789123456789000000000' +
        '0012345678901234576890' |
        'SEALED:V1:3c8oJx0t2gN/xuOgXi2y9nP3GkULKcaR72d9jrA2kFjHG0RtZYFyVb5UrNCsNhHUdcB1ZbnqEp/uZgMYkUjM4p7u/V7q8UgBJBzbK8l4Y7LxC4cva/+LD9NOJMEfX3lFMLMOICG128OkVpPsgJYjK5OlBBSsOsVWn2X7XfzdjTXlJq13RtzPqKjyjmMH/Wi9'
  }

  def '暗号化されたパスワードが、正しく復号できること'(String password, String sealed) {
    expect:
    PasswordUtil.unseal(sealed) == password.getBytes()

    where:
    password                     | sealed
    'some password'              | 'SEALED:V1:1bB3EbMcYByendrEC4vraA=='
    ''                           | 'SEALED:V1:5+IVMWze0DvH/D4ASnPCfg=='
    '1'                          | 'SEALED:V1:/NSAZuGWFvggJPa5aQI27g=='
    'シール'                        | 'SEALED:V1:BFWYEOZKpYWP1l6v5RLX0A=='
    'sealed:A'                   | 'SEALED:V1:fSPdLoWYap3r1qpOa/Talg=='
    '123456789123456789123456789' +
        '123456789123456789123456789' +
        '123456789123456789123456789' +
        '123456789123456789000000000' +
        '0012345678901234576890' |
        'SEALED:V1:3c8oJx0t2gN/xuOgXi2y9nP3GkULKcaR72d9jrA2kFjHG0RtZYFyVb5UrNCsNhHUdcB1ZbnqEp/uZgMYkUjM4p7u/V7q8UgBJBzbK8l4Y7LxC4cva/+LD9NOJMEfX3lFMLMOICG128OkVpPsgJYjK5OlBBSsOsVWn2X7XfzdjTXlJq13RtzPqKjyjmMH/Wi9'
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
