package makemkv

import org.specs2.mutable.Specification

/**
 * Created by alex on 07/05/15.
 */
class FilenameSanitiserSpec extends Specification with FilenameSanitiser {

  "Upper case characters" should {
    "be replaced by lower case characters" in {
      sanitise("AB") must be equalTo("ab")
    }
  }

  "Lower case characters" should {
    "stay the same" in {
      sanitise("AB") must be equalTo("ab")
    }
  }

  "Digits" should {
    "stay the same" in {
      sanitise("543") must be equalTo("543")
    }
  }

  "Punctuation" should {
    "be removed" in {
      sanitise("!,()") must be equalTo("")
    }
  }

  "Whitespace" should {
    "be normalised to a single underscore" in {
      sanitise("ab \tcd") must beEqualTo("ab_cd")
    }
  }
}
