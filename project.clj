(defproject mb-compojure "0.1.0-SNAPSHOT"
  :description "Compojure based REST API for the MartenBlog"
  :url "http://martenblog.herokuapp.com"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [com.novemberain/monger "1.6.0-beta3"]
                 [ring/ring-json "0.2.0"]
                 [cheshire "5.2.0"]]
  :plugins [[lein-ring "0.8.5"]]
  :ring {:handler mb-compojure.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
