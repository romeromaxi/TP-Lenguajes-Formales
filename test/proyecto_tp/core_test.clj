(ns proyecto-tp.core-test
  (:require [clojure.test :refer :all]
            [proyecto-tp.core :refer :all]
            [proyecto-tp.enunciado :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; TESTS FUNCIONES IMPLEMENTADAS PARA QUE ANDE EL INTERPRETE DE PL/0 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(deftest a-mayusculas-salvo-strings-test
  (testing "Prueba de la funcion: a-mayusculas-salvo-strings"
    (is (= "" (a-mayusculas-salvo-strings nil)))
    (is (= "  CONST Y = 2;" (a-mayusculas-salvo-strings "  const Y = 2;")))
    (is (= "  WRITELN ('Se ingresa un valor, se muestra su doble.');" (a-mayusculas-salvo-strings "  writeln ('Se ingresa un valor, se muestra su doble.');")))
  )
)

(deftest cadena?-test
  (testing "Prueba de la funcion: cadena?"
    (is (= false (cadena? nil)))
    (is (= true (cadena? "'Hola'")))
    (is (= false (cadena? "Hola")))
    (is (= false (cadena? "'Hola")))
    (is (= false (cadena? 'Hola)))
    (is (= true (cadena? "'Pruebaconunacadenamaslarga'")))
  )
)

(deftest palabra-reservada?-test
  (testing "Prueba de la funcion: palabra-reservada?"
    (is (= false (palabra-reservada? nil)))
    (is (= true (palabra-reservada? 'CALL)))
    (is (= true (palabra-reservada? "CALL")))
    (is (= false (palabra-reservada? 'ASIGNAR)))
    (is (= false (palabra-reservada? "ASIGNAR")))
    (is (= false (palabra-reservada? '1)))
    (is (= false (palabra-reservada? true)))
    (is (= true (palabra-reservada? "CONST")))
    (is (= true (palabra-reservada? "VAR")))
    (is (= true (palabra-reservada? "PROCEDURE")))
    (is (= true (palabra-reservada? "BEGIN")))
    (is (= true (palabra-reservada? "END")))
    (is (= true (palabra-reservada? "IF")))
    (is (= true (palabra-reservada? "THEN")))
    (is (= true (palabra-reservada? "WHILE")))
    (is (= true (palabra-reservada? "DO")))
    (is (= true (palabra-reservada? "ODD")))
    (is (= true (palabra-reservada? "READLN")))
    (is (= true (palabra-reservada? "WRITELN")))
    (is (= true (palabra-reservada? "WRITE")))
  )
)

(deftest identificador?-test
  (testing "Prueba de la funcion: identificador?"
    (is (= false (identificador? nil)))
    (is (= false (identificador? 2)))
    (is (= true (identificador? 'V2)))
    (is (= true (identificador? "V2")))
    (is (= false (identificador? 'CALL)))
    (is (= false (identificador? "CALL")))
    (is (= false (identificador? "#WHILE")))
    (is (= false (identificador? "V:2")))
    (is (= false (identificador? "procedure")))
  )
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; TEST FUNCIONES AUXILIARES 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(deftest es-numero-como-caracter?-test
  (testing "Prueba de la funcion: es-numero-como-caracter?"
    (is (= false (es-numero-como-caracter? nil)))
    (is (= true (es-numero-como-caracter? "2")))
    (is (= true (es-numero-como-caracter? \2)))
  )
)

(deftest contiene-simbolos?-test
  (testing "Prueba de la funcion: contiene-simbolos?"
    (is (= false (contiene-simbolos? nil)))
    (is (= false (contiene-simbolos? "CadenaSinSimbolos")))
    (is (= true (contiene-simbolos? "Cont#ene")))
    (is (= true (contiene-simbolos? '%principio)))
    (is (= true (contiene-simbolos? "v:a>r=i{o$s;")))
  )
)