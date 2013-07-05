(ns mb-compojure.util
  (:require [monger.query :as q]))

(defn last-id [coll]
  (let [last-id (q/with-collection coll
                  (q/limit 1)
                  (q/sort (sorted-map :_id -1))
                  (q/fields [:_id])
                  (q/find {}))]
    (if (empty? last-id) 0 (-> (first last-id)
                               (:_id)))))

(defn next-id [coll]
  (inc (last-id coll)))
