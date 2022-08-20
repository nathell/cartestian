(ns cartestian.example
  (:require [cartestian.core]
            [cartestian.test :refer [with-combinations]]
            [#?(:clj clojure.test :cljs cljs.test) :as t]))

(t/deftest example
  (with-combinations [variant {:this [:success :failure]
                               :that [:success :failure]}]
    (t/is (= (:this variant) (:that variant)))))
