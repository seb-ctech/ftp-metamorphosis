; 3. Exercise: composing a working glsl piece with two or three layers
(ns exercises.glsl-parsing)

(def return-break "\n\n")
(def open-block "{")
(def close-block "}")

(defn glsl-prefix []
    (str "#ifdef GL_ES\n" 
         "precision mediump float;\n" 
         "#endif" 
         return-break 
         "uniform vec2 u_resolution;\n" 
         "uniform float u_time;" 
         return-break))

;TODO: implement function to generate a single statement entry
; Transform clojure lisp syntax to GLSL valid syntax: infix operations, function calls with arguments...
(defn generate-statement [])


;DOC: Trying out a function parsing algorithms with keys that dictate a parsing syntax: :type return, statement, control(nested), declaration, call
; Statements can be: function calls, expressions/operations and assignments;
(def test-function [
    {:type :parameter, :data-type "int", :name "a"}
    {:type :declaration, :data-type "int", :name "v"}
    {:type :control, :statement (list 
        (list "if" {:type :expression :statement "u_time > 50"}) 
        (list {:type :assignment, :name "v" :statement (list
            "v +" {:type :name :index 0})}))}
    {:type :control, :statement (list
        (list "else") 
        (list {:type :assignment, :name "v" :statement (list
                {:type :expression :statement "20 +"} 
                {:type :call :statement (list "add" "2" "4")})}))}
    {:type :control, :statement (list
        (list "for" 
            {:type :declaration :data-type "int" :name "i" :statement "0" }
            {:type :statement :statement {:type :expression :statement "i < 50"}}
            {:type :assignment :name "i" :statement {:type :expression :statement "i + 1"}})
        {:type :assignment :name "v" :statement "30"})}
    {:type :return :name "v"}])

(def control-test {:type :control, :statement (list
    (list "for" 
        {:type :declaration :data-type "int" :name "i" :statement "0" }
        {:type :statement :statement {:type :expression :statement "i < 50"}}
        {:type :assignment :name "i" :statement {:type :expression :statement "i + 1"}})
    {:type :assignment :name "v" :statement "30"})})

(def main-function [
    {:type :statement, :statement "gl_FragColor = <nested1>;"}
    {:type :call, :statement "vec4(1, 1, 1, 1)"}
])

(defn get-parsed-parameters [statements]
    (let [params (filter #(= (:type %) :parameter) statements)]
        (str "(" (clojure.string/join ", " (map #(str (:data-type %) " " (:name %)) params)) ")")))

(defn get-return-type [statements]
    (let [type-statements (filter #(or (= (:type %) :declaration) (= (:type %) :parameter)) statements)
          returned-variable (:name (first (filter #(= (:type %) :return) statements)))
          matched-names (filter #(= returned-variable (:name %)) type-statements)]
        (if (> (count matched-names) 0)
            (:data-type (first matched-names))
            "void")))

;TODO: Find recursive reduction algorithm to compose complex statements
(defn compose-statement [statement]
    (if (list? statement)
        (reduce #(str %1 (compose-statement %2)) "" statement)
        (if (map? statement)
            (case (:type statement)
                :declaration (str (:data-type statement) " " (:name statement) (if (contains? statement :statement) (str " = " (compose-statement (:statement statement)))) ";")
                :assignment (str (:name statement) " = " (compose-statement (:statement statement)) ";")
                :return (str "return" " " (compose-statement (:name statement)) ";")
                :statement (str (compose-statement (:statement statement)) ";")
                :expression (str (compose-statement (:statement statement)))
                :call (str (first (:statement statement)) "(" (clojure.string/join ", " (map compose-statement (rest (:statement statement)))) ")")
                :name (str "<param" (:index statement) ">")
                :control (let [head (first (:statement statement))
                               body (rest (:statement statement))]
                            (str (str (first head) "(" (compose-statement (rest head)) ")")
                                 (str open-block return-break (compose-statement body) return-break close-block)))
                "")
            statement)))

(defn compose-statements [statements]
    (let [valid-types [:assignment :control :call :expression :statement :declaration :return]]
        (map compose-statement (filter (fn [statement] (some #(= (:type statement) %) valid-types)) statements) )))

(defn replace-parameters [glsl-string statements]
    (let [parameters (into [] (filter #(= (:type %) :parameter) statements))
         parameter-calls (distinct (re-seq #"<param\d>" glsl-string))
         parameter-indexes (map #(let [num (Integer/parseInt (re-find #"\d" %))] (list num (:name (get parameters num)))) parameter-calls)]
        (reduce #(clojure.string/replace %1 (re-pattern (str "<param" (first %2) ">")) (last %2)) glsl-string parameter-indexes)))

(defn glsl-function [name statements]
    (let [return-type (get-return-type statements)
          parameter-list (get-parsed-parameters statements)
          composed-statements (compose-statements statements)]
        (str return-type " " name parameter-list open-block return-break
            (replace-parameters (apply str composed-statements) statements) return-break close-block)))

(defn glsl-functions [functions]
    (apply str functions))

(defn glsl-main [statements]
    (glsl-function "main" statements))

(defn assemble-glsl []
    (str (glsl-prefix)(glsl-functions [test-function])(glsl-main main-function)))

(defn test [] (println (glsl-function "test" test-function)))