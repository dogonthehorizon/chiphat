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

(defn- response-body
  "Private helper function to get the json body out of a request."
  [response]
  (-> (get-in response [:opts :body])
      json/parse-string))

(deftest get-all-functionality
  (with-fake-http [{:url "https://api.hipchat.com/v2/room"
                    :method :get}
                   {:status 200}]
    (testing "get-all should return all the current rooms."
      (let [response @(room/get-all {:start-index 0
                                     :max-results 1
                                     :include-archived false})
            query-params (get-in response [:opts :query-params])]
        (is (= 0 (:start-index query-params)))
        (is (= 1 (:max-results query-params)))
        (is (not (:include-archived query-params)))))))

(deftest get-one-functionality
  (with-fake-http [{:url "https://api.hipchat.com/v2/room/foobar"
                    :method :get}
                   {:status 200
                    :body get-response-body}]
    (testing "get-one should retrieve information about an existing room."
      (let [response (parse-response (room/get-one "foobar"))]
        (is (= 969638 (:id response)))
        (is (= "foobar" (:name response)))))))

(deftest create-functionality
  (with-fake-http [{:url "https://api.hipchat.com/v2/room"
                    :method :post}
                   {:status 201
                    :body create-response-body}]
    (testing "create should create a new room."
      (is (= 975185 (:id (parse-response (room/create "foobar"))))))))

(deftest delete-functionality
  (with-fake-http [{:url "https://api.hipchat.com/v2/room/foobar"
                    :method :delete}
                   {:status 200
                    :body nil}]
    (testing "delete should delete an existing room."
      (is (= nil (parse-response (room/delete "foobar")))))))

(deftest recent-room-history-functionality
  (with-fake-http [{:url "https://api.hipchat.com/v2/room/foobar/history/latest"
                    :method :get}
                   {:status 200}]
    (testing "recent-room-history should return the most recent history for an
             existing room and respect optionally passed in parameters."
      (let [response @(room/recent-room-history "foobar" {:max-results 1})
            body (response-body response)
            max-results (get body "max-results")]
        (is (= 200 (:status response)))
        (is (= 1 max-results))))))

(deftest send-notification-functionality
  (with-fake-http [{:url "https://api.hipchat.com/v2/room/foobar/notification"
                    :method :post}
                   {:status 204
                    :body nil}]
    (testing "send-notification should post a notification to an existing room."
      (let [response @(room/send-notification "foobar"
                                              "hello world"
                                              {:color "red"
                                               :notify true})
            body (response-body response)
            message (get body "message")
            color (get body "color")
            notify? (get body "notify")]
        (is (= 204 (:status response)))
        (is (= color "red"))
        (is notify?)
        (is (= message "hello world"))))))

(deftest send-reply-functionality
  (with-fake-http [{:url "https://api.hipchat.com/v2/room/foobar/reply"
                    :method :post}
                   {:status 204
                    :body nil}]
    (let [response @(room/send-reply "foobar"
                                     "f0d8a8bf-d006-489a-a05e-9f76457a1c39"
                                     "hello world!")
          body (response-body response)
          message (get body "message")]
      (is (= 204 (:status response)))
      (is (= "hello world!" message)))))

(deftest share-link-functionality
  (with-fake-http [{:url "https://api.hipchat.com/v2/room/foobar/share/link"
                    :method :post}
                   {:status 204
                    :body nil}]
    (let [response @(room/share-link "foobar"
                                     "https://www.google.com"
                                     "sending a test link")
          body (response-body response)
          link (get body "link")
          message (get body "message")]
      (is (= 204 (:status response)))
      (is (= "sending a test link" message))
      (is (= "https://www.google.com" link)))))
