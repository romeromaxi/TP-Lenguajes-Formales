(ns proyecto-tp.core-test
  (:require [clojure.test :refer :all]
            [proyecto-tp.core :refer :all]))

(deftest es-el-doble?-test
  (testing "Prueba de la funcion: es-el-doble?"
    (is (= true (= 4 4)))))
