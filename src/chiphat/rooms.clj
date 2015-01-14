(ns chiphat.rooms
  (:refer-clojure :exclude [update])
  (:require [chiphat.core :refer :all]
            [chiphat.util :refer [url-encode]]))

(def base-endpoint
  "room")

(defn get-all
  "Get all available rooms."
  [& [{:keys [start-index max-results include-private include-archived] :as opts}]]
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

(defn update
  "Updates a room."
  [name-or-id
   {:keys [privacy is_archived is_guest_accessible topic owner] :as opts}]
  (let [options (assoc opts :name name-or-id)]
    (make-request
      :put
      (str base-endpoint "/" (url-encode name-or-id))
      {:body options})))

(defn recent-history
  "Fetch the latest chat history for this room."
  [name-or-id & [{:keys [max-results timezone not-before] :as opts}]]
  (make-request :get
                (str base-endpoint "/" (url-encode name-or-id) "/history/latest")
                {:body opts}))

(defn history
  "Fetch chat room history for this room."
  [name-or-id &
   [{:keys [date timezone start-index max-results reverse] :as opts}]]
  (make-request
    :get
    (str base-endpoint "/" (url-encode name-or-id) "/history")
    {:query opts}))

(defn get-message
  "Fetch one specific message by id."
  [name-or-id message-id & [{:keys [timezone]}]]
  (make-request
    :get
    (str base-endpoint "/" (url-encode name-or-id) "/history/" message-id)
    {:query {:timezone timezone}}))

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

(defn share-link
  "Share a link with a given room."
  [name-or-id link message]
  (make-request :post
                (str base-endpoint "/" (url-encode name-or-id) "/share/link")
                {:body {:link link
                        :message message}}))
