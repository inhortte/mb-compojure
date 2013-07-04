(ns mb-compojure.db
  (:require [monger.core       :as mg]
            [monger.query      :as query]
            [mb-compojure.util :as util])
  (:import [com.mongodb MongoOptions ServerAddress]))

(let [^MongoOptions opts (mg/mongo-options :threads-allowed-to-block-for-connection-multiplier 300)
      ^ServerAddress sa  (mg/server-address "127.0.0.1" 27017)]
  (mg/connect! sa opts)
  (mg/set-db! (mg/get-db "martenblog")))

(defn get-all-entries [])

(defn get-page [page]
  {:body
   (query/with-collection "entry"
     (query/find {})
     (query/sort (sorted-map :created_at -1))
     (query/fields [:subject])
     (query/paginate :page (Integer/parseInt page) :per-page 11))})

(defn create-new-entry [body])

;; macro here?
(defn get-entry-by-id [id]
  {:body
   (let [entry (query/with-collection "entry"
                 (query/find {:_id (if (string? id)
                                     (Integer/parseInt id) id)}))]
     (if (empty? entry) '() (first entry)))})

(defn get-entries [ids]
  {:body (map #(:body (get-entry-by-id %)) ids)})

(defn update-entry [id body])

(defn destroy-entry [id])

(defn get-topic-by-id [id]
  {:body
   (let [topic (query/with-collection "topic"
                 (query/find {:_id (if (string? id)
                                     (Integer/parseInt id) id)}))]
     (if (empty? topic) '() (first topic)))})

(defn get-topics [ids]
  {:body (map #(:body (get-topic-by-id %)) ids)})

(defn get-topics-by-entry [id]
  (-> (get-entry-by-id id)
      (:body)
      (:topic_ids)
      (get-topics)))

(defn get-entries-by-topic [id]
  (-> (get-topic-by-id id)
      (:body)
      (:entry_ids)
      (get-entries)))

(defn set-topic [id]
  )
