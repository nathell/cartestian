(ns cartestian.example
  (:require [cartestian.core]
            [cartestian.test :refer [with-combinations]]
            [#?(:clj clojure.test :cljs cljs.test) :as t]))

(defn make-dimensions [n]
  (for [i (range n)]
    {:name (keyword (str "dim" i)), :dimension [:opt1 :opt2]}))

(t/deftest example
  (with-combinations [variant (make-dimensions 10)]
    ;; Testing 100 combinations out of 1024. We fail if
    ;; all of them are :opt1, so it has about 0.1 chance of
    ;; failing.
    (t/is (not (every? #(= % :opt1) (vals variant)))))
  (with-combinations [variant (make-dimensions 10) {:count :all}]
    ;; But this will surely fail.
    (t/is (not (every? #(= % :opt1) (vals variant))))))
