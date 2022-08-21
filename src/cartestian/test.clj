(ns cartestian.test)

;; ideas for config:
;; :count – upper limit of executed tests, can be :all or a number, defaults to 100 (?)
;; :randomize – randomize the order of tests, defaults to true
;; :when – only run tests when the variant matches a given predicate (doesn't eat up :count if not); run all by default
;; :prefix – prefix of the t/testig

(defmacro with-combinations
  [[sym dimensions & [config]] & body]
  `(let [cp# (cartestian.core/combinations ~dimensions ~config)]
     (doseq [~sym cp#]
       (~(if (:ns &env) ; yuck https://groups.google.com/g/clojurescript/c/iBY5HaQda4A/m/w1lAQi9_AwsJ
           `cljs.test/testing
           `clojure.test/testing)
        (pr-str ~sym)
        ~@body))))
