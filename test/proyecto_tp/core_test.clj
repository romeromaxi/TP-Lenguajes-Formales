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
    (is (= "MAYUSCULA 'minuscula' MAYUSCULA" (a-mayusculas-salvo-strings "mayuscula 'minuscula' mayuscula")))
    (is (= "MAYUSCULA 'esto es una subcadena' CALL 'otra subcadena' PROCEDURE '" (a-mayusculas-salvo-strings "mayuscula 'esto es una subcadena' call 'otra subcadena' procedure '")))
    (is (= "MAYUSCULA 'subcadena con numero 2' VAR X = 2" (a-mayusculas-salvo-strings "mayuscula 'subcadena con numero 2' var x = 2")))
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
    (is (= false (identificador? "pro cedure")))
    (is (= false (identificador? "")))
    (is (= false (identificador? "nom var")))
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

(deftest ya-declarado-localmente?-test
  (testing "Prueba de la funcion: ya-declarado-localmente?"
    (is (= true (ya-declarado-localmente? 'Y '[[0] [[X VAR 0] [Y VAR 1]]])))
    (is (= false (ya-declarado-localmente? 'Z '[[0] [[X VAR 0] [Y VAR 1]]])))
    (is (= false (ya-declarado-localmente? 'Y '[[0 3 5] [[X VAR 0] [Y VAR 1] [INICIAR PROCEDURE 1] [Y CONST 2] [ASIGNAR PROCEDURE 2]]])))
    (is (= true (ya-declarado-localmente? 'Y '[[0 3 5] [[X VAR 0] [Y VAR 1] [INICIAR PROCEDURE 1] [Y CONST 2] [ASIGNAR PROCEDURE 2] [Y CONST 6]]])))
    (is (= true (ya-declarado-localmente? 'X '[[0] [[X VAR 0] [Y VAR 1]]])))
    (is (= false (ya-declarado-localmente? 'X '[[0 1 9] [[X VAR 0] [Y VAR 1]]])))
  )
)

(deftest cargar-var-en-tabla-test
  (testing "Prueba de la funcion: cargar-var-en-tabla"
    (is (= '[nil () [VAR X] :error [[0] []] 0 [[JMP ?]]] (cargar-var-en-tabla '[nil () [VAR X] :error [[0] []] 0 [[JMP ?]]])))
    (is (= '[nil () [VAR X] :sin-errores [[0] [[X VAR 0]]] 1 [[JMP ?]]] (cargar-var-en-tabla '[nil () [VAR X] :sin-errores [[0] []] 0 [[JMP ?]]])))
    (is (= '[nil () [VAR X Y] :sin-errores [[0] [[X VAR 0] [Y VAR 1]]] 2 [[JMP ?]]] (cargar-var-en-tabla '[nil () [VAR X , Y] :sin-errores [[0] [[X VAR 0]]] 1 [[JMP ?]]])))
    (is (= '[nil () [VAR X Y] :sin-errores [[0] [[Y VAR 0]]] 1 [[JMP ?]]] (cargar-var-en-tabla '[nil () [VAR X , Y] :sin-errores [[0] []] 0 [[JMP ?]]])))
  )
)

(deftest inicializar-contexto-local-test
  (testing "Prueba de la funcion: inicializar-contexto-local"
    (is (= '[nil () [] :error [[0] [[X VAR 0] [Y VAR 1] [INI PROCEDURE 1]]] 2 [[JMP ?]]] (inicializar-contexto-local '[nil () [] :error [[0] [[X VAR 0] [Y VAR 1] [INI PROCEDURE 1]]] 2 [[JMP ?]]])))
    (is (= '[nil () [] :sin-errores [[0 3] [[X VAR 0] [Y VAR 1] [INI PROCEDURE 1]]] 2 [[JMP ?]]] (inicializar-contexto-local '[nil () [] :sin-errores [[0] [[X VAR 0] [Y VAR 1] [INI PROCEDURE 1]]] 2 [[JMP ?]]])))
  )
)

(deftest declaracion-var-test
  (testing "Prueba de la funcion: declaracion-var"
    (is (= ['VAR (list 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=") 7 (symbol ";") 'Y (symbol ":=") 12 (symbol ";") 'END (symbol ".")) [] :error [[0] []] 0 '[[JMP ?]]] (declaracion-var ['VAR (list 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=") 7 (symbol ";") 'Y (symbol ":=") 12 (symbol ";") 'END (symbol ".")) [] :error [[0] []] 0 '[[JMP ?]]])))
    (is (= ['BEGIN (list 'X (symbol ":=") 7 (symbol ";") 'Y (symbol ":=") 12 (symbol ";") 'END (symbol ".")) ['VAR 'X (symbol ",") 'Y (symbol ";")] :sin-errores '[[0] [[X VAR 0] [Y VAR 1]]] 2 '[[JMP ?]]] (declaracion-var ['VAR (list 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=") 7 (symbol ";") 'Y (symbol ":=") 12 (symbol ";") 'END (symbol ".")) [] :sin-errores [[0] []] 0 '[[JMP ?]]])))
  )
)

(deftest procesar-signo-unario-test
  (testing "Prueba de la funcion: procesar-signo-unario"
    (is (= ['+ (list 7 (symbol ";") 'Y ':= '- 12 (symbol ";") 'END (symbol ".")) ['VAR 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=")] :error '[[0] [[X VAR 0] [Y VAR 1]]] 2 []] (procesar-signo-unario ['+ (list 7 (symbol ";") 'Y ':= '- 12 (symbol ";") 'END (symbol ".")) ['VAR 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=")] :error '[[0] [[X VAR 0] [Y VAR 1]]] 2 []])))
    (is (= "[7 (; Y := - 12 ; END .) [VAR X , Y ; BEGIN X :=] :sin-errores [[0] [[X VAR 0] [Y VAR 1]]] 2 []]" (str (procesar-signo-unario [7 (list (symbol ";") 'Y ':= '- 12 (symbol ";") 'END (symbol ".")) ['VAR 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=")] :sin-errores '[[0] [[X VAR 0] [Y VAR 1]]] 2 []]))))
    (is (= "[7 (; Y := - 12 ; END .) [VAR X , Y ; BEGIN X := +] :sin-errores [[0] [[X VAR 0] [Y VAR 1]]] 2 []]" (str (procesar-signo-unario ['+ (list 7 (symbol ";") 'Y ':= '- 12 (symbol ";") 'END (symbol ".")) ['VAR 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=")] :sin-errores '[[0] [[X VAR 0] [Y VAR 1]]] 2 []]))))
    (is (= "[7 (; Y := - 12 ; END .) [VAR X , Y ; BEGIN X := -] :sin-errores [[0] [[X VAR 0] [Y VAR 1]]] 2 []]" (str (procesar-signo-unario ['- (list 7 (symbol ";") 'Y ':= '- 12 (symbol ";") 'END (symbol ".")) ['VAR 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=")] :sin-errores '[[0] [[X VAR 0] [Y VAR 1]]] 2 []]))))
    (is (= ['* (list 7 (symbol ";") 'Y ':= '- 12 (symbol ";") 'END (symbol ".")) ['VAR 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=")] :sin-errores '[[0] [[X VAR 0] [Y VAR 1]]] 2 []] (procesar-signo-unario ['* (list 7 (symbol ";") 'Y ':= '- 12 (symbol ";") 'END (symbol ".")) ['VAR 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=")] :sin-errores '[[0] [[X VAR 0] [Y VAR 1]]] 2 []])))
  )
)

(deftest termino-test
  (testing "Prueba de la funcion: termino"
    (is (= ['X (list '* 2 'END (symbol ".")) ['VAR 'X (symbol ";") 'BEGIN 'X (symbol ":=")] :error '[[0] [[X VAR 0]]] 1 []] (termino ['X (list '* 2 'END (symbol ".")) ['VAR 'X (symbol ";") 'BEGIN 'X (symbol ":=")] :error '[[0] [[X VAR 0]]] 1 []])))
    (is (= "[END (.) [VAR X ; BEGIN X := X * 2] :sin-errores [[0] [[X VAR 0]]] 1 [[PFM 0] [PFI 2] MUL]]" (str (termino ['X (list '* 2 'END (symbol ".")) ['VAR 'X (symbol ";") 'BEGIN 'X (symbol ":=")] :sin-errores '[[0] [[X VAR 0]]] 1 []]))))
  )
)

(deftest expresion-test
  (testing "Prueba de la funcion: expresion"
    (is (= ['- (list (symbol "(") 'X '* 2 '+ 1 (symbol ")") 'END (symbol ".")) ['VAR 'X (symbol ";") 'BEGIN 'X (symbol ":=")] :error '[[0] [[X VAR 0]]] 1 []] (expresion ['- (list (symbol "(") 'X '* 2 '+ 1 (symbol ")") 'END (symbol ".")) ['VAR 'X (symbol ";") 'BEGIN 'X (symbol ":=")] :error '[[0] [[X VAR 0]]] 1 []])))
    (is (= "[END (.) [VAR X ; BEGIN X := + ( X * 2 + 1 )] :sin-errores [[0] [[X VAR 0]]] 1 [[PFM 0] [PFI 2] MUL [PFI 1] ADD]]" (str (expresion ['+ (list (symbol "(") 'X '* 2 '+ 1 (symbol ")") 'END (symbol ".")) ['VAR 'X (symbol ";") 'BEGIN 'X (symbol ":=")] :sin-errores '[[0] [[X VAR 0]]] 1 []]))))
    (is (= "[END (.) [VAR X ; BEGIN X := - ( X * 2 + 1 )] :sin-errores [[0] [[X VAR 0]]] 1 [[PFM 0] [PFI 2] MUL [PFI 1] ADD NEG]]" (str (expresion ['- (list (symbol "(") 'X '* 2 '+ 1 (symbol ")") 'END (symbol ".")) ['VAR 'X (symbol ";") 'BEGIN 'X (symbol ":=")] :sin-errores '[[0] [[X VAR 0]]] 1 []]))))
  )
)

(deftest aplicar-aritmetico-test
  (testing "Prueba de la funcion: aplicar-aritmetico"
    (is (= [3] (aplicar-aritmetico + [1 2])))
    (is (= [1 3] (aplicar-aritmetico - [1 4 1])))
    (is (= [1 8] (aplicar-aritmetico * [1 2 4])))
    (is (= [1 0] (aplicar-aritmetico / [1 2 4])))
    (is (= nil (aplicar-aritmetico + nil)))
    (is (= [] (aplicar-aritmetico + [])))
    (is (= [1] (aplicar-aritmetico + [1])))
    (is (= [1 2 4] (aplicar-aritmetico 'hola [1 2 4])))
    (is (= [1 2 4] (aplicar-aritmetico count [1 2 4])))
    (is (= '[a b c] (aplicar-aritmetico + '[a b c])))
    (is (= '[1 4 1 a] (aplicar-aritmetico - '[1 4 1 a])))
    (is (= '[9 8 K 0] (aplicar-aritmetico + '[9 8 K 0])))
    (is (= '#{a c b} (aplicar-aritmetico + (hash-set 'a 'b 'c))))
    (is (= '(1 2 3 4) (aplicar-aritmetico * '(1 2 3 4))))
  )
)

(deftest aplicar-relacional-test
  (testing "Prueba de la funcion: aplicar-relacional"
    (is (= [1] (aplicar-relacional > [7 5])))
    (is (= [4 1] (aplicar-relacional > [4 7 5])))
    (is (= [4 0] (aplicar-relacional = [4 7 5])))
    (is (= [4 1] (aplicar-relacional not= [4 7 5])))
    (is (= [4 0] (aplicar-relacional < [4 7 5])))
    (is (= [4 1] (aplicar-relacional <= [4 6 6])))
    (is (= '[a b c] (aplicar-relacional <= '[a b c])))
    (is (= nil (aplicar-relacional = nil)))
    (is (= [1 2 4] (aplicar-relacional / [1 2 4])))
    (is (= [] (aplicar-relacional > [])))
    (is (= [1] (aplicar-relacional not= [1])))
    (is (= [1 2 4] (aplicar-relacional 'hola [1 2 4])))
    (is (= [1 2 4] (aplicar-relacional count [1 2 4])))
    (is (= '#{a c b} (aplicar-relacional < (hash-set 'a 'b 'c))))
    (is (= '[1 4 1 a] (aplicar-relacional = '[1 4 1 a])))
    (is (= '[9 8 K 0] (aplicar-relacional = '[9 8 K 0])))
    (is (= '(1 2 3 4) (aplicar-relacional > '(1 2 3 4))))

  )
)

; dump

(deftest generar-test
  (testing "Prueba de la funcion: generar"
    (is (= '[nil () [VAR X] :sin-errores [[0] []] 0 [[JMP ?] HLT]] (generar '[nil () [VAR X] :sin-errores [[0] []] 0 [[JMP ?]]] 'HLT)))
    (is (= '[nil () [VAR X] :sin-errores [[0] []] 0 [[JMP ?] [PFM 0]]] (generar '[nil () [VAR X] :sin-errores [[0] []] 0 [[JMP ?]]] 'PFM 0)))
    (is (= '[nil () [VAR X] :error [[0] []] 0 [[JMP ?]]] (generar '[nil () [VAR X] :error [[0] []] 0 [[JMP ?]]] 'HLT)))
    (is (= '[nil () [VAR X] :error [[0] []] 0 [[JMP ?]]] (generar '[nil () [VAR X] :error [[0] []] 0 [[JMP ?]]] 'PFM 0)))
  )
)

(deftest buscar-coincidencias-test
  (testing "Prueba de la funcion: buscar-coincidencias"
    (is (= '([X VAR 0] [X VAR 2]) (buscar-coincidencias '[nil () [CALL X] :sin-errores [[0 3] [[X VAR 0] [Y VAR 1] [A PROCEDURE 1] [X VAR 2] [Y VAR 3] [B PROCEDURE 2]]] 6 [[JMP ?] [JMP 4] [CAL 1] RET]])))
    (is (= '([Y VAR 1] [Y VAR 3]) (buscar-coincidencias '[nil () [CALL X CALL Z CALL Y] :sin-errores [[0 3] [[X VAR 0] [Y VAR 1] [A PROCEDURE 1] [X VAR 2] [Y VAR 3] [B PROCEDURE 2]]] 6 [[JMP ?] [JMP 4] [CAL 1] RET]])))
    (is (= '() (buscar-coincidencias '[nil () [CALL Z] :sin-errores [[0 3] [[X VAR 0] [Y VAR 1] [A PROCEDURE 1] [X VAR 2] [Y VAR 3] [B PROCEDURE 2]]] 6 [[JMP ?] [JMP 4] [CAL 1] RET]])))
  )
)

(deftest fixup-test
  (testing "Prueba de la funcion: fixup"
    (is (= '[WRITELN (END .) [] :error [[0 3] []] 6 [[JMP ?] [JMP ?] [CAL 1] RET]] (fixup ['WRITELN (list 'END (symbol ".")) [] :error [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] 1)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6 [[JMP ?] [JMP 4] [CAL 1] RET]] (fixup ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] 1)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6 [[JMP 8] [JMP 4] [CAL 1] RET [PFM 2] OUT NL RET]] (fixup ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] [JMP 4] [CAL 1] RET [PFM 2] OUT NL RET]] 0)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6 [[JMP ?] OUT [CAL 1] RET]] (fixup ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] OUT [CAL 1] RET]] 1)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6 [[JMP ?] [CAL 1] RET]] (fixup ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] [CAL 1] RET]] 1)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6 [[JMP ?] [JMP ?] [CAL 1] RET]] (fixup ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] 8)))
  )
)

(deftest generar-operador-relacional-test
  (testing "Prueba de la funcion: generar-operador-relacional"
    (is (= '[WRITELN (END .) [] :error [[0 3] []] 6 [[JMP ?] [JMP ?] [CAL 1] RET]] (generar-operador-relacional ['WRITELN (list 'END (symbol ".")) [] :error [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] '=)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6 [[JMP ?] [JMP ?] [CAL 1] RET]] (generar-operador-relacional ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] '+)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6 [[JMP ?] [JMP ?] [CAL 1] RET EQ]] (generar-operador-relacional ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] '=)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6 [[JMP ?] [JMP ?] [CAL 1] RET GTE]] (generar-operador-relacional ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] '>=)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6 [[JMP ?] [JMP ?] [CAL 1] RET GT]] (generar-operador-relacional ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] '>)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6 [[JMP ?] [JMP ?] [CAL 1] RET LT]] (generar-operador-relacional ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] '<)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6 [[JMP ?] [JMP ?] [CAL 1] RET LTE]] (generar-operador-relacional ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] '<=)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6 [[JMP ?] [JMP ?] [CAL 1] RET NEQ]] (generar-operador-relacional ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] '<>)))
  )
)

(deftest generar-signo-test
  (testing "Prueba de la funcion: generar-signo"
    (is (= '[nil () [] :error [[0] [[X VAR 0]]] 1 [MUL ADD]] (generar-signo [nil () [] :error '[[0] [[X VAR 0]]] 1 '[MUL ADD]] '-)))
    (is (= '[nil () [] :error [[0] [[X VAR 0]]] 1 [MUL ADD]] (generar-signo [nil () [] :error '[[0] [[X VAR 0]]] 1 '[MUL ADD]] '+)))
    (is (= '[nil () [] :sin-errores [[0] [[X VAR 0]]] 1 [MUL ADD]] (generar-signo [nil () [] :sin-errores '[[0] [[X VAR 0]]] 1 '[MUL ADD]] '+)))
    (is (= '[nil () [] :sin-errores [[0] [[X VAR 0]]] 1 [MUL ADD]] (generar-signo [nil () [] :sin-errores '[[0] [[X VAR 0]]] 1 '[MUL ADD]] '*)))
    (is (= '[nil () [] :sin-errores [[0] [[X VAR 0]]] 1 [MUL ADD NEG]] (generar-signo [nil () [] :sin-errores '[[0] [[X VAR 0]]] 1 '[MUL ADD]] '-)))
    (is (= '[nil () [] :sin-errores [[0] [[X VAR 0] [Y VAR 1]]] 2 [[CAL 1] RET MUL ADD NEG]] (generar-signo [nil () [] :sin-errores '[[0] [[X VAR 0] [Y VAR 1]]] 2 '[[CAL 1] RET MUL ADD]] '-)))
    (is (= '[nil () [] :sin-errores [[0] [[X VAR 0] [Y VAR 1]]] 2 [[CAL 1] RET MUL ADD]] (generar-signo [nil () [] :sin-errores '[[0] [[X VAR 0] [Y VAR 1]]] 2 '[[CAL 1] RET MUL ADD]] '+)))
    (is (= '[nil () [] :sin-errores [[0] [[X VAR 0]]] 1 [MUL ADD NEG]] (generar-signo [nil () [] :sin-errores '[[0] [[X VAR 0]]] 1 '[MUL ADD]] "-")))
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

(deftest no-es-string?-test
  (testing "Prueba de la funcion: no-es-string?"
    (is (= true (no-es-string? true)))
    (is (= true (no-es-string? 123)))
    (is (= false (no-es-string? "Cadena")))
    (is (= false (no-es-string? 'string)))
    (is (= true (no-es-string? nil)))
    (is (= true (no-es-string? '5)))
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

(deftest ultimos-dos-elementos-numericos?-test
  (testing "Prueba de la funcion: ultimos-dos-elementos-numericos?"
    (is (= true (ultimos-dos-elementos-numericos? [1 2 3 4 5 6])))
    (is (= false (ultimos-dos-elementos-numericos? '[1 2 3 4 5 6 a])))
    (is (= false (ultimos-dos-elementos-numericos? '[1 2 K 3])))
    (is (= false (ultimos-dos-elementos-numericos? nil)))
    (is (= false (ultimos-dos-elementos-numericos? [])))
    (is (= false (ultimos-dos-elementos-numericos? [1])))
    (is (= true (ultimos-dos-elementos-numericos? '[a b c d e f g h i 14 13])))
  )
)

(deftest es-operador-aritmetico-diadico?-test
  (testing "Prueba de la funcion: es-operador-aritmetico-diadico?"
    (is (= true (es-operador-aritmetico-diadico? +)))
    (is (= true (es-operador-aritmetico-diadico? -)))
    (is (= true (es-operador-aritmetico-diadico? *)))
    (is (= true (es-operador-aritmetico-diadico? /)))
    (is (= false (es-operador-aritmetico-diadico? max)))
    (is (= false (es-operador-aritmetico-diadico? inc)))
    (is (= false (es-operador-aritmetico-diadico? count)))
    (is (= false (es-operador-aritmetico-diadico? map)))
    (is (= false (es-operador-aritmetico-diadico? >)))
    (is (= true (es-operador-aritmetico-diadico? '+)))
    (is (= true (es-operador-aritmetico-diadico? "*")))
  )
)

(deftest es-operador-relacional-clojure?-test
  (testing "Prueba de la funcion: es-operador-relacional-clojure?"
    (is (= true (es-operador-relacional-clojure? =)))
    (is (= true (es-operador-relacional-clojure? not=)))
    (is (= true (es-operador-relacional-clojure? <)))
    (is (= true (es-operador-relacional-clojure? <=)))
    (is (= true (es-operador-relacional-clojure? >)))
    (is (= true (es-operador-relacional-clojure? >=)))
    (is (= false (es-operador-relacional-clojure? +)))
    (is (= false (es-operador-relacional-clojure? -)))
    (is (= false (es-operador-relacional-clojure? max)))
    (is (= false (es-operador-relacional-clojure? inc)))
    (is (= true (es-operador-relacional-clojure? 'not=)))
    (is (= false (es-operador-relacional-clojure? '<>)))
    (is (= true (es-operador-relacional-clojure? ">=")))
  )
)

(deftest es-operador-relacional-pl0?-test
  (testing "Prueba de la funcion: es-operador-relacional-pl0?"
    (is (= true (es-operador-relacional-pl0? =)))
    (is (= false (es-operador-relacional-pl0? not=)))
    (is (= true (es-operador-relacional-pl0? <)))
    (is (= true (es-operador-relacional-pl0? <=)))
    (is (= true (es-operador-relacional-pl0? >)))
    (is (= true (es-operador-relacional-pl0? >=)))
    (is (= false (es-operador-relacional-pl0? +)))
    (is (= false (es-operador-relacional-pl0? -)))
    (is (= false (es-operador-relacional-pl0? max)))
    (is (= false (es-operador-relacional-pl0? inc)))
    (is (= false (es-operador-relacional-pl0? 'not=)))
    (is (= true (es-operador-relacional-pl0? '<>)))
    (is (= true (es-operador-relacional-pl0? ">=")))
  )
)

(deftest es-operador-monadico-de-signo?-test
  (testing "Prueba de la funcion: es-operador-monadico-de-signo?"
    (is (= true (es-operador-monadico-de-signo? '+)))
    (is (= true (es-operador-monadico-de-signo? '-)))
    (is (= false (es-operador-monadico-de-signo? '*)))
    (is (= false (es-operador-monadico-de-signo? '/)))
    (is (= true (es-operador-monadico-de-signo? +)))
    (is (= true (es-operador-monadico-de-signo? -)))
    (is (= false (es-operador-monadico-de-signo? *)))
    (is (= false (es-operador-monadico-de-signo? /)))
    (is (= true (es-operador-monadico-de-signo? "+")))
    (is (= true (es-operador-monadico-de-signo? "-")))
    (is (= false (es-operador-monadico-de-signo? "*")))
    (is (= false (es-operador-monadico-de-signo? "/")))
  )
)

(deftest es-operador-monadico-signo-negativo?-test
  (testing "Prueba de la funcion: es-operador-monadico-signo-negativo?"
    (is (= true (es-operador-monadico-signo-negativo? '-)))
    (is (= false (es-operador-monadico-signo-negativo? '+)))
    (is (= false (es-operador-monadico-signo-negativo? '*)))
    (is (= false (es-operador-monadico-signo-negativo? '/)))
    (is (= true (es-operador-monadico-signo-negativo? -)))
    (is (= false (es-operador-monadico-signo-negativo? +)))
    (is (= false (es-operador-monadico-signo-negativo? *)))
    (is (= false (es-operador-monadico-signo-negativo? /)))
    (is (= true (es-operador-monadico-signo-negativo? "-")))
    (is (= false (es-operador-monadico-signo-negativo? "+")))
    (is (= false (es-operador-monadico-signo-negativo? "*")))
    (is (= false (es-operador-monadico-signo-negativo? "/")))
  )
)
