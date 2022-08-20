(ns cartestian.core
  (:require #?(:clj [cartestian.product]
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
