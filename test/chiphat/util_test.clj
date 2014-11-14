(ns chiphat.util-test
  (:require [clojure.test :refer :all]
            [chiphat.util :as util]))

(deftest url-encode-functionality
  (testing "url-encode should escape a string with spaces"
    (let [encoded (util/url-encode "foo bar baz quux")]
      (is (= encoded "foo%20bar%20baz%20quux")))))
