(ns chiphat.rooms-test
  (:require [clojure.test :refer :all]
            [org.httpkit.fake :refer [with-fake-http]]
            [cheshire.core :as json]
            [chiphat.core :refer :all]
            [chiphat.rooms :as room]))

(def create-response-body
  "{\"id\":975185,\"links\": {\"self\":\"https://api.hipchat.com/v2/room/975185\"}}")

(def get-response-body
  "{\"created\": \"2014-11-14T16:30:17+00:00\", \"guest_access_url\": null,
  \"id\": 969638, \"is_archived\": false, \"is_guest_accessible\": false,
  \"last_active\": null, \"links\": {\"participants\":
  \"https://api.hipchat.com/v2/room/969638/participant\", \"self\":
  \"https://api.hipchat.com/v2/room/969638\", \"webhooks\":
  \"https://api.hipchat.com/v2/room/969638/webhook\"}, \"name\": \"foobar\",
  \"owner\": {\"id\": 474911, \"links\": {\"self\":
  \"https://api.hipchat.com/v2/user/474911\"}, \"mention_name\": \"ffreire\",
  \"name\": \"Fernando Freire\"}, \"participants\": [], \"privacy\":
  \"public\", \"statistics\": {\"links\": {\"self\":
  \"https://api.hipchat.com/v2/room/969638/statistics\"}}, \"topic\": \"\",
  \"xmpp_jid\": \"37252_foobar@conf.hipchat.com\"}")

(deftest get-all-functionality
  (testing "get-all should return all the current rooms."
    (with-token (atom (System/getenv "CHIPHAT_API_TOKEN"))
      (let [response (parse-response (room/get-all))]
        (is (map? response))
        (is (> (count (:items response)) 0))))))

(deftest create-functionality
  (with-fake-http [{:url "https://api.hipchat.com/v2/room"
                    :method :post}
                   {:status 201
                    :body create-response-body}]
    (testing "create should create a new room."
      (with-token (atom (System/getenv "CHIPHAT_API_TOKEN"))
        (is (= 975185 (:id (parse-response (room/create "foobar")))))))))

(deftest delete-functionality
  (with-fake-http [{:url "https://api.hipchat.com/v2/room/foobar"
                    :method :delete}
                   {:status 200
                    :body nil}]
    (testing "delete should delete an existing room."
      (with-token (atom (System/getenv "CHIPHAT_API_TOKEN"))
        (is (= nil (parse-response (room/delete "foobar"))))))))

(deftest get-one-functionality
  (with-fake-http [{:url "https://api.hipchat.com/v2/room/foobar"
                    :method :get}
                   {:status 200
                    :body get-response-body}]
    (testing "get-one should retrieve information about an existing room."
      (with-token (atom (System/getenv "CHIPHAT_API_TOKEN"))
        (let [response (parse-response (room/get-one "foobar"))]
          (is (= 969638 (:id response)))
          (is (= "foobar" (:name response))))))))
