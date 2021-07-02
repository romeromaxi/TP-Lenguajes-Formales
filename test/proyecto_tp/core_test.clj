(ns proyecto-tp.core-test
  (:require [clojure.test :refer :all]
            [proyecto-tp.core :refer :all]))

(deftest es-el-doble?-test
  (testing "Prueba de la funcion: es-el-doble?"
    (is (= true (es-el-doble? 4 8)))
    (is (= false (es-el-doble? 4 7)))))
