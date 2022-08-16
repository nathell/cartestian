(ns cartestian.test
  (:require [cartestian.core :as core]
            [clojure.test :as t]))

;; ideas for config:
;; :count – upper limit of executed tests, can be :all or a number, defaults to 100 (?)
;; :randomize – randomize the order of tests, defaults to true
;; :when – only run tests when the variant matches a given predicate (doesn't eat up :count if not); run all by default
;; :prefix – prefix of the t/testig

(defmacro with-combinations
  [[sym dimensions & [config]] & body]
  `(let [cp# (core/cartesian-product ~dimensions)]
     (doseq [~sym cp#]
       (t/testing (pr-str ~sym)
         ~@body))))

(comment
  (t/deftest example
    (with-combinations [variant {:this [:success :failure]
                                 :that [:success :failure]}]
      (t/is (= (:this variant) (:that variant))))))
