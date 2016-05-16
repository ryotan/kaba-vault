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
    password | sealed
    'some password' | 'SEALED:V1:8c+21uvsX27JqwwrmDfcQA=='
    '' | 'SEALED:V1:CDkY7YDxvuw='
    '1' | 'SEALED:V1:hPb6NpSc1bc='
    'シール' | 'SEALED:V1:G4PEVg0j+G8rXS/jZoDF2w=='
    'sealed:A' | 'SEALED:V1:spI6J3UOsP41TtzoUxr+pA=='
    '1234567891234567891234567891234567891234567891234567891234567891234567891234567891234567891234567890000000000012345678901234576890' |
        'SEALED:V1:ukMBAa2beypIFr90/tXmnkfUmn0JLVPnirTz8/d5thkIIjJwiAeG1BNoPi9LLTDBP48fYxQD7WanEKDxgmGqS+5NE740qZ0R2CKWRKmScqUjokZK4x9uhodMDZNn7bovgfvco8N94C+Gl9NQLQ00C/8W9/2ADlzdfm4pMbIF8I8URneAlm9csw=='
  }

  def '暗号化されたパスワードが、正しく復号ること'(String password, String sealed) {
    expect:
    PasswordUtil.unseal(sealed) == password.getBytes()

    where:
    password | sealed
    'some password' | 'SEALED:V1:8c+21uvsX27JqwwrmDfcQA=='
    '' | 'SEALED:V1:CDkY7YDxvuw='
    '1' | 'SEALED:V1:hPb6NpSc1bc='
    'シール' | 'SEALED:V1:G4PEVg0j+G8rXS/jZoDF2w=='
    'sealed:A' | 'SEALED:V1:spI6J3UOsP41TtzoUxr+pA=='
    '1234567891234567891234567891234567891234567891234567891234567891234567891234567891234567891234567890000000000012345678901234576890' |
      'SEALED:V1:ukMBAa2beypIFr90/tXmnkfUmn0JLVPnirTz8/d5thkIIjJwiAeG1BNoPi9LLTDBP48fYxQD7WanEKDxgmGqS+5NE740qZ0R2CKWRKmScqUjokZK4x9uhodMDZNn7bovgfvco8N94C+Gl9NQLQ00C/8W9/2ADlzdfm4pMbIF8I8URneAlm9csw=='
    '12345' | '12345'
    '' | ''
  }

  def "暗号化されたパスワードを複合できること"(String password) {
    expect:
    new String(PasswordUtil.unseal(PasswordUtil.seal(password.getBytes()))) == password

    where:
    password | _
    'some password' | _
    '' | _
    '1' | _
    'シール' | _
    'sealed:A' | _
    '1234567891234567891234567891234567891234567891234567891234567891234567891234567891234567891234567890000000000012345678901234576890' | _
  }

  def "カバレッジツールがprivate constructorをignoreしてくれればいいのに。。。"() {
    def util = new PasswordUtil();

    expect:
    util != null
  }
}
