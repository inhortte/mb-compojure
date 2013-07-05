(ns mb-compojure.db
  (:require [monger.core       :as mg]
            [monger.query      :as q]
            [mb-compojure.util :as util])
  (:import [com.mongodb MongoOptions ServerAddress]))

(let [^MongoOptions opts (mg/mongo-options :threads-allowed-to-block-for-connection-multiplier 300)
      ^ServerAddress sa  (mg/server-address "127.0.0.1" 27017)]
  (mg/connect! sa opts)
  (mg/set-db! (mg/get-db "martenblog")))

(defn get-all-entries [])

(defn get-page [page]
  {:body
   (q/with-collection "entry"
     (q/find {})
     (q/sort (sorted-map :created_at -1))
     (q/fields [:subject])
     (q/paginate :page (Integer/parseInt page) :per-page 11))})

(defn create-new-entry [body])

;; macro here?
(defn get-entry-by-id [id]
  {:body
   (let [entry (q/with-collection "entry"
                 (q/find {:_id (if (string? id)
                                     (Integer/parseInt id) id)}))]
     (if (empty? entry) '() (first entry)))})

(defn get-entries [ids]
  {:body (map #(:body (get-entry-by-id %)) ids)})

(defn get-last-entry []
  (get-entry-by-id (util/last-id "entry")))

(defn update-entry [id body])

(defn destroy-entry [id])

(defn get-topic-by-id [id]
  {:body
   (let [topic (q/with-collection "topic"
                 (q/find {:_id (if (string? id)
                                     (Integer/parseInt id) id)}))]
     (if (empty? topic) '() (first topic)))})

(defn get-topics [ids]
  {:body (map #(:body (get-topic-by-id %)) ids)})

(defn get-all-topics []
  {:body
   (->> (q/with-collection "topic" (q/find {}))
        (sort-by #(- (count (:entry_ids %)))))})

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

(defn create-new-topic [body])

(defn update-topic [id body])

(defn destroy-topic [id])

(defn set-topic [id]
  )
