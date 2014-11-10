(ns chiphat.core)

(def base-url
  "https://api.hipchat.com/v2")

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
     ~@body))
