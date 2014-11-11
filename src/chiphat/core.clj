
(ns chiphat.core
  (:require [org.httpkit.client :as http]))

(def base-url
  "https://api.hipchat.com/v2/")

(def ^:dynamic *token*
  "Required auth token for interacting with HipChat."
  (atom nil))

(defn set-token!
  "Replace the current token with the given input."
  [new-token]
  (reset! *token* new-token))

(defmacro with-token
  "Evaluates body with token temporarily set to a new token."
  [new-token & body]
  `(binding [*token* ~new-token]
     (do
       ~@body)))

(defn make-request
  "Given an endpoint, make a request to the HipChat API."
  [endpoint & [options-map]]
  (let [query-params (merge {:auth_token @*token*} options-map)]
    (http/get (str base-url endpoint) {:query-params query-params})))
