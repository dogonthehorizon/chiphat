(defproject chiphat "0.5.0"
  :description "Clojure library for accessing the HipChat v2 API."
  :url "https://github.com/dogonthehorizon/chiphat"
  :license {:name "BSD"
            :url "http://opensource.org/licenses/BSD-3-Clause"}
  :dependencies [[org.clojure/clojure "1.7.0-alpha5"]
                 [http-kit            "2.1.19"]
                 [cheshire            "5.4.0"]]
  :plugins [[lein-marginalia "0.8.0"]]
  :profiles {:dev {:dependencies [[http-kit.fake "0.2.2"]
                                  [org.clojure/tools.namespace  "0.2.9"]]
                   :source-paths ["dev/"]}})
