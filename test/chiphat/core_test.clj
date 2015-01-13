(ns chiphat.core-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as json]
            [chiphat.core :refer :all]
            [org.httpkit.fake :refer [with-fake-http]]))

(def troll-response
  (json/generate-string
    {:creator {:id 656379
               :links {:self "https://api.hipchat.com/v2/user/656379"}
               :mention_name "clifton"
               :name "Clifton Hensley"}
     :height 25
     :id 105148
     :links {:self "https://api.hipchat.com/v2/emoticon/105148"}
     :shortcut "troll"
     :url "https://dujrsrsgsd3nh.cloudfront.net/img/emoticons/troll-1414023002.png"
     :width 25}))

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
    (with-fake-http [{:url "https://api.hipchat.com/v2/emoticon/fry"
                      :method :get}
                     {:status 200}]
      (let  [{:keys  [status opts]} @(make-request :get  "emoticon/fry")]
        (is (= 200 status))
        (is (= "application/json" (get (:headers opts) "Content-Type")))))))

(deftest parse-response-functionality
  (testing "When a response is returned from the HipChat api, parse-response
           should return a map representation of the response."
    (with-fake-http [{:url "https://api.hipchat.com/v2/emoticon/troll"
                      :method :get}
                     {:status 200
                      :body troll-response}]
      (let [response (make-request :get "emoticon/troll")
            response-map (parse-response response)
            status (:status @response)]
        (is (= 200 status))
        (is (map? response-map))
        (is (= "troll" (:shortcut response-map))))))
  (testing "parse-reseponse should return a success map with a message if a
           204 with no content is received from the HipChat API."
    (with-fake-http [{:url "https://api.hipchat.com/v2/room/test-room"
                     :method :delete}
                     {:status 204
                      :body nil}]
      (let [response-map (parse-response
                           (make-request :delete "room/test-room"))]
        (is (map? response-map))
        (is (empty? response-map))))))
