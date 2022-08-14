(ns cartestian.core)

(deftype CartesianProductSeq
    [product i]
  clojure.lang.ISeq
  (first [_]
    (nth product i))
  (next [_]
    (let [i' (inc i)]
      (when (< i' (count product))
        (CartesianProductSeq. product i'))))
  (more [this]
    (or (.next this)
        (clojure.lang.PersistentList/EMPTY)))
  (cons [this o]
    (clojure.lang.Cons. o this))
  (equiv [this o]
    (cond
      (identical? this o) true
      (or (instance? clojure.lang.Sequential o) (instance? java.util.List o))
      (loop [me this
             you (seq o)]
        (if (nil? me)
          (nil? you)
          (and (clojure.lang.Util/equiv (first me) (first you))
               (recur (next me) (next you)))))
      :else false))
  (empty [_]
    clojure.lang.PersistentList/EMPTY)
  clojure.lang.Seqable
  (seq [this] this))

(deftype CartesianProduct
    [dimensions]
  clojure.lang.Counted
  (count [_]
    (apply * (map (comp count :dimension) dimensions)))
  clojure.lang.Indexed
  (nth [_ i]
    (->
     (reduce
      (fn [[n v] {:keys [name dimension]}]
        (let [cnt (count dimension)
              k (mod n cnt)
              n' (quot n cnt)]
          [n' (assoc! v name (nth dimension k))]))
      [i (transient {})]
      dimensions)
     second
     persistent!))
  clojure.lang.Seqable
  (seq [this]
    (CartesianProductSeq. this 0)))

(defn- map->dimension-list [m]
  (mapv (fn [[k v]]
          {:name k, :dimension v})
        m))

(defn cartesian-product [dimensions]
  (CartesianProduct.
   (cond-> dimensions
     (map? dimensions) map->dimension-list)))
