(ns clojure.core-test.apply
  (:require [clojure.test :refer [deftest testing are]]
            [clojure.core-test.portability #?(:cljs :refer-macros :default :refer) [when-var-exists]]))

(when-var-exists apply
  (deftest test-apply

    (testing "apply f args"
      (let [f str]
        (are [expected actual] (= expected actual)
                               (f) (apply f nil)
                               (f) (apply f [])
                               (f) (apply f '())
                               (f) (apply f #{})
                               (f) (apply f "")
                               (f 1 2 3 4) (apply f [1 2 3 4])
                               (f 1 2 3 4) (apply f '(1 2 3 4))
                               (f 1 2 3 4) (apply f (sorted-set 1 2 3 4))
                               (f [1 2] [3 4]) (apply f {1 2, 3 4})
                               (f \1 \2 \3 \4) (apply f "1234"))))

    (testing "apply f x args"
      (let [f conj]
        (are [expected actual] (= expected actual)
                               (f [1]) (apply f [1] nil)
                               (f [1]) (apply f [1] [])
                               (f [1] 2 3 4) (apply f [1] [2 3 4]))))

    (testing "apply f x y args"
      (let [f -]
        (are [expected actual] (= expected actual)
                               (f 1 2) (apply f 1 2 nil)
                               (f 1 2) (apply f 1 2 '())
                               (f 1 2 3 4) (apply f 1 2 '(3 4)))))

    (testing "apply f x y z args"
      (let [f +]
        (are [expected actual] (= expected actual)
                               (f 1 2 3) (apply f 1 2 3 nil)
                               (f 1 2 3) (apply f 1 2 3 #{})
                               (f 1 2 3 4) (apply f 1 2 3 #{4}))))

    (testing "apply f a b c d & args"
      (let [f str]
        (are [expected actual] (= expected actual)
                               (f 1 2 3 4) (apply f 1 2 3 4 "")
                               (f 1 2 3 4 \5 \6) (apply f 1 2 3 4 "56"))))

    (testing "supports ifn"
      (are [expected ifn args] (= expected (apply ifn args))
                               nil :1 [0]
                               1 [0 1 2] [1]
                               2 #{0 1 2} [2]))

    (testing "bad shape"
      (are [f args] (thrown? #?(:cljs js/Error :default Exception) (apply f args))
                    nil []
                    42 []
                    3.14 []
                    "s" []
                    true []
                    false []
                    str 42
                    str 3.14
                    str true
                    str false))))
