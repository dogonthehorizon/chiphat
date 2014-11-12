(ns chiphat.emoticons
  (:require [chiphat.core :refer :all]))

(def base-endpoint
  "emoticon")

(defn get-all
  "Get all available emoji."
  []
  (make-request base-endpoint))

(defn get-one
  "Given an id or shortcut, get the details for an emoticon."
  [id-or-shortcut]
  (make-request (str base-endpoint "/" id-or-shortcut)))
