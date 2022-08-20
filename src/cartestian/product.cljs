(ns cartestian.product)

(deftype CartesianProductSeq
    [product i]
  ISeq
  (-first [_]
    (nth product i))
  (-rest [this]
    (or (-next this)
        (empty [])))
  INext
  (-next [_]
    (let [i' (inc i)]
      (when (< i' (count product))
        (CartesianProductSeq. product i'))))
  ISeqable
  (-seq [this] this))

(deftype CartesianProduct
    [dimensions]
  ICounted
  (-count [_]
    (apply * (map (comp count :dimension) dimensions)))
  IIndexed
  (-nth [_ i]
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
  ISeqable
  (-seq [this]
    (CartesianProductSeq. this 0)))
