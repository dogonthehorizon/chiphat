(ns chiphat.users
  (:require
    [chiphat
      [core :refer :all]
      [util :refer [url-encode]]]))

(def base-endpoint
  "user")

(defn view
 "Get a user's details."
 [id-or-email]
 (make-request :get (str base-endpoint "/" (url-encode id-or-email))))
