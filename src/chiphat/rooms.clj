(ns chiphat.rooms
  (:require [chiphat.core :refer :all]
            [chiphat.util :refer [url-encode]]))

(def base-endpoint
  "room")

(defn get-all
  "Get all available rooms."
  [& [{:keys [start-index max-results include-archived] :as opts}]]
  (make-request :get base-endpoint {:query opts}))

(defn create
  "Create a new room."
  [room-name & [{:keys [topic guest_access owner_user_id privacy] :as opts}]]
  (let [body (assoc opts :name room-name)]
    (make-request :post base-endpoint {:body body})))

(defn delete
  "Delete an existing room."
  [name-or-id]
  (make-request :delete (str base-endpoint "/" name-or-id)))

(defn get-one
  "Get room details."
  [name-or-id]
  (make-request :get (str base-endpoint "/" (url-encode name-or-id))))
