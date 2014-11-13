(ns chiphat.emoticons
  (:require [chiphat.core :refer :all]))

(def base-endpoint
  "emoticon")

(defn get-all
  "Get all available emoji."
  []
  (make-request :get base-endpoint))

(defn get-one
  "Given an id or shortcut, get the details for an emoticon."
  [id-or-shortcut]
  (make-request :get (str base-endpoint "/" id-or-shortcut)))
