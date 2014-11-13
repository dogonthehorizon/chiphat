(ns chiphat.core
  (:require [org.httpkit.client :as http]
            [cheshire.core :as json]
            [clojure.walk :refer [keywordize-keys]]))

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
  "Given a method, endpoint, and optional query and body maps, make a request
  to the HipChat API."
  [method endpoint & [{:keys [query body]}]]
  (let [query-params (merge {:auth_token @*token*} query)
        request-body (if (map? body) (json/generate-string body))]
    (http/request {:url (str base-url endpoint)
                   :method method
                   :query-params query-params
                   :body request-body})))

(defn parse-response
  "Given a response from the HipChat API, parse the body and return a map."
  [response]
  (-> @response
      :body
      json/parse-string
      keywordize-keys))
