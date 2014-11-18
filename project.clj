(defproject chiphat "0.1.1"
  :description "Clojure library for accessing the HipChat v2 API."
  :url "https://github.com/dogonthehorizon/chiphat"
  :license {:name "BSD"
            :url "http://opensource.org/licenses/BSD-3-Clause"}
  :dependencies [[org.clojure/clojure "1.7.0-alpha3"]
                 [http-kit            "2.1.18"]
                 [cheshire            "5.3.1"]]
  :profiles {:dev {:dependencies [[http-kit.fake "0.2.2"]]}})
