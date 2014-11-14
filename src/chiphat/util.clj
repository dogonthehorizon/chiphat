(ns chiphat.util
  (:import (java.net URLEncoder)))

(defn url-encode
  "Given a string, return a url encoded version of that string."
  ^{:author "Chas Emerick"
    :src "https://github.com/cemerick/url/blob/master/src/cemerick/url.cljx"}
  [s]
  (some-> s str (URLEncoder/encode "UTF-8") (.replace "+" "%20")))
