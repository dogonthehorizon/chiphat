(ns chiphat.core-test
  (:require [clojure.test :refer :all]
            [chiphat.core :refer :all]))

(deftest token-functionality
  (testing "set-token! should set the token to the provided value."
    (let [token-val "foo"]
      (set-token! token-val)
      (is (= token-val @*token*))))
  (testing "with-token should temporarily change the value of *token*."
    (let [token-val "bar"]
      (set-token! token-val)
      (with-token (atom "baz")
        (is (= "baz" @*token*)))
      (is (= token-val @*token*)))))

(deftest make-request-functionality
  (testing "Given an endpoint and an api token, make-request should return a 200"
    (set-token! (System/getenv "CHIPHAT_API_TOKEN"))
    (let [{:keys [status]} @(make-request "emoticon")]
      (is (= 200 status)))))
