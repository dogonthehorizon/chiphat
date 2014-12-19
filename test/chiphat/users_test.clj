(ns chiphat.users-test
  (:require
    [clojure.test :refer :all]
    [org.httpkit.fake :refer [with-fake-http]]
    [chiphat
      [core :refer :all]
      [util :refer [url-encode]]
      [users :as user]]))

(deftest view-functionality
  (with-fake-http [{:url "https://api.hipchat.com/v2/user/%40ffreire"
                    :method :get}
                   {:status 200}]
    (let [response @(user/view "@ffreire")]
      (is (= 200 (:status response))))))
