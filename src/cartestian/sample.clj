(ns cartestian.sample)

(defn- how-many-to-skip [top Nreal]
  (let [V (rand)]
    (loop [s 0 top top Nreal Nreal q (/ (double top) Nreal)]
      (if (<= q V)
        s
        (recur (inc s) (dec top) (dec Nreal) (/ (* q (dec top)) (dec Nreal)))))))

(defn method-a [k N]
  (->>
   (reduce
    (fn [[v curr Nreal top] k]
      (let [s (how-many-to-skip top Nreal)
            nxt (+ curr s)]
        [(conj! v nxt) (inc nxt) (- Nreal s 1) (- top s)]))
    [(transient []) 0 N (- N k)]
    (range k 0 -1))
   first
   persistent!))
