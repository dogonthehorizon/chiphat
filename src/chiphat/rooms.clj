(ns chiphat.rooms
  (:refer-clojure :exclude [update])
  (:require [chiphat.core :refer :all]
            [chiphat.util :refer [url-encode]]))

(def base-endpoint
  "room")

(defn get-all
  "Get all available rooms."
  [& [{:keys [start-index max-results include-archived] :as opts}]]
  (make-request :get base-endpoint {:query opts}))

(defn get-one
  "Get room details."
  [name-or-id]
  (make-request :get (str base-endpoint "/" (url-encode name-or-id))))

(defn create
  "Create a new room."
  [room-name & [{:keys [topic guest_access owner_user_id privacy] :as opts}]]
  (let [body (assoc opts :name room-name)]
    (make-request :post base-endpoint {:body body})))

(defn delete
  "Delete an existing room."
  [name-or-id]
  (make-request :delete (str base-endpoint "/" (url-encode name-or-id))))

(defn recent-room-history
  "Fetch the latest chat history for this room."
  [name-or-id & [{:keys [max-results timezone not-before] :as opts}]]
  (make-request :get
                (str base-endpoint "/" (url-encode name-or-id) "/history/latest")
                {:body opts}))

(defn send-notification
  "Send a message to a room."
  [name-or-id message & [{:keys [color notify message_format] :as opts}]]
  (let [body (assoc opts :message message)]
    (make-request :post
                  (str base-endpoint "/" (url-encode name-or-id) "/notification")
                  {:body body})))

(defn send-reply
  "Send a reply to a room."
  [name-or-id parent-message-id message]
  (make-request :post
                (str base-endpoint "/" (url-encode name-or-id) "/reply")
                {:body {:parentMessageId parent-message-id
                        :message message}}))
