(ns mb-compojure.handler
  (:require [compojure.core :refer :all]
            [cheshire.core  :refer :all])
  (:require [compojure.handler    :as handler]
            [compojure.route      :as route]
            [ring.middleware.json :as middleware]
            [mb-compojure.db      :as db]))

(defroutes app-routes
  (context "/blog" []
           (defroutes blog-routes
             ;; (GET     "/" []           (db/get-all-entries))
             ;; (POST    "/" {body :body} (db/create-new-entry body))
             (context "/rutabaga" []
                      (defroutes rutabaga-routes
                        (GET "/" []              (db/get-page 1))))
             (context "/rutabaga/:page" [page]
                      (defroutes rutabaga-page-routes
                        (GET "/" []              (db/get-page page))))
             (context "/entry" []
                      (defroutes entries-routes
                        (GET  "/" []             (db/get-last-entry))
                        (POST "/" {body :body}   (db/create-new-entry body))))
             (context "/entry/:id" [id]
                      (defroutes entry-routes
                        (GET    "/" []           (db/get-entry-by-id id))
                        (PUT    "/" {body :body} (db/update-entry id body))
                        (DELETE "/" []           (db/destroy-entry id))))
             (GET "/entry/:id/topics" [id]       (db/get-topics-by-entry id))
             (context "/topic" []
                      (defroutes topics-routes
                        (GET  "/" []             (db/get-all-topics))
                        (POST "/" {body :body}   (db/create-new-topic body))))
             (context "/topic/:id" [id]
                      (defroutes topic-routes
                        (GET    "/" []           (db/get-topic-by-id id))
                        (PUT    "/" {body :body} (db/update-topic id body))
                        (DELETE "/" []           (db/destroy-topic id))))
             (GET "/topic/:id/entries" [id]      (db/get-entries-by-topic id))))
  (route/not-found "The angry mustelid has gone awry"))

(def app
  (->
   (handler/api app-routes)
   (middleware/wrap-json-body)
   (middleware/wrap-json-params)
   (middleware/wrap-json-response)))
