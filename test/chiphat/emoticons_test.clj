(ns chiphat.emoticons-test
  (:require [clojure.test :refer :all]
            [chiphat.core :refer :all]
            [chiphat.emoticons :as emoji]))

(deftest get-all-functionality
  (testing "get-all should return all emoji for the current user's group."
    (with-token (atom (System/getenv "CHIPHAT_API_TOKEN"))
      (let [emoji (parse-response (emoji/get-all))]
        (is (map? emoji))
        (is (> (count (:items emoji)) 0))))))

(deftest get-one-functionality
  (testing "Given a valid emoticon shortcut, get-one should return information
           about that emoticon."
    (with-token (atom (System/getenv "CHIPHAT_API_TOKEN"))
      (let [shortcut "ohgodwhy"
            oh-god-why (parse-response (emoji/get-one shortcut))]
        (is (map? oh-god-why))
        (is (= (:shortcut oh-god-why) shortcut)))))
  (testing "Given a valid emoticon id, get-one should return information about
           that emoticon."
    (with-token (atom (System/getenv "CHIPHAT_API_TOKEN"))
      (let [id 105228
            fry (parse-response (emoji/get-one id))]
        (is (map? fry))
        (is (= (:id fry) id)))))
  (testing "Given an invalid value, get-one should return an error from the
           HipChat API."
    (with-token (atom (System/getenv "CHIPHAT_API_TOKEN"))
      (let [invalid (parse-response (emoji/get-one "as87gf08qb34fljasdf"))]
        (is (map? invalid))
        (is (= (get-in invalid [:error :code]) 404))))))
