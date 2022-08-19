(ns cartestian.sample)

(defn- how-many-to-skip [top n]
  (let [v (rand)]
    (loop [s 0 q (/ (double top) n)]
      (if (<= q v)
        s
        (recur (inc s) (/ (* q top) n))))))

(defn method-a [n max]
  (->>
   (reduce
    (fn [[v curr nreal top] n]
      (let [s (how-many-to-skip top nreal)
            nxt (+ curr s 1)]
        [(conj! v nxt) nxt (- nreal s) (- top s)]))
    [(transient []) 0 max (- max n)]
    (range n 2 -1))
   first
   persistent!))
