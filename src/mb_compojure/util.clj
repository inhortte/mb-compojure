(ns mb-compojure.util
  (:require [monger.query :as query]))

(defn next-id [coll]
  (let [last-id (query/with-collection coll
                  (query/limit 1)
                  (query/sort (sorted-map :_id -1))
                  (query/fields [:_id])
                  (query/find {}))]
    (if (empty? last-id) 1 (-> (first last-id)
                               (:_id)
                               (inc)))))
