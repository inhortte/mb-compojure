(ns mb-compojure.handler
  (:require [compojure.core :refer :all]
            [cheshire.core  :refer :all])
  (:require [compojure.handler    :as handler]
            [compojure.route      :as route]
            [ring.middleware.json :as middleware]
            [mb-compojure.db      :as db]))

(defroutes app-routes
  (context "/blog" []
           (defroutes entries-routes
             (GET     "/" []           (db/get-all-entries))
             (POST    "/" {body :body} (db/create-new-entry body))
             (context "/rutabaga" []
                      (defroutes rutabaga-routes
                        (GET "/" [] (db/get-page 1))
                        (context "/:page" [page]
                                 (defroutes rutabaga-page-routes
                                   (GET "/" [] (db/get-page page))))))
             (context "/entry/:id" [id]
                      (defroutes entry-routes
                        (GET    "/" []           (db/get-entry-by-id id))
                        (PUT    "/" {body :body} (db/update-entry id body))
                        (DELETE "/" []           (db/destroy-entry id))))))
  (route/not-found "The angry mustelid has gone awry"))

(def app
  (->
   (handler/api app-routes)
   (middleware/wrap-json-body)
   (middleware/wrap-json-params)
   (middleware/wrap-json-response)))
