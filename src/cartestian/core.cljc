(ns cartestian.core
  (:require [cartestian.sample :as sample]
            #?(:clj [cartestian.product]
               :cljs [cartestian.product :refer [CartesianProduct]]))
  #?(:clj (:import [cartestian.product CartesianProduct])))

(defn- map->dimension-list [m]
  (mapv (fn [[k v]]
          {:name k, :dimension v})
        m))

(defn cartesian-product [dimensions]
  (CartesianProduct.
   (cond-> dimensions
     (map? dimensions) map->dimension-list)))

(def default-config
  {:count 100})

(defn combinations [dimensions config]
  (let [product (cartesian-product dimensions)
        config (merge default-config config)
        n (count product)]
    (if (or (= (:count config) :all)
            (<= n (:count config)))
      product
      (let [indices (sample/method-a (:count config) n)]
        (map (partial nth product) indices)))))
