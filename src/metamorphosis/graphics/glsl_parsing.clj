; This contains a parsing algorithm that translate an intermediate meta-data structure to valid GLSL-ES Code
(ns metamorphosis.graphics.glsl-parsing)

(def return-break "\n\n")
(def open-block "{")
(def close-block "}")
(def glsl-folder "resources/glsl/")

(defn glsl-prefix []
    (str "#ifdef GL_ES\n" 
         "precision mediump float;\n" 
         "#endif" 
         return-break 
         "uniform vec2 u_resolution;\n" 
         "uniform float u_time;" 
         return-break))

;TODO: implement function to inject nested functions and derive the necessary data-structure (will build the interface to the l-system)
; Transform clojure lisp syntax to GLSL valid syntax: infix operations, function calls with arguments...
(defn generate-statement [])

;=============== TEST DATA =========================

;DOC: Trying out a function parsing algorithm with keys that dictate a parsing syntax: :type return, statement, control(nested), declaration, call
; Statements can be: function calls, expressions/operations and assignments;
(def test-function {:name "a" :function [
    {:type :parameter, :data-type "float", :name "a"}
    {:type :declaration, :data-type "float", :name "v"}
    {:type :control, :statement (list 
        (list "if" {:type :expression :statement "u_time > 50.0"}) 
        (list {:type :statement :statement {:type :assignment, :name "v" :statement (list
            "v +" {:type :name :index 0})}}))}
    {:type :control, :statement (list
        "else"
        (list {:type :statement :statement {:type :assignment, :name "v" :statement (list
                {:type :expression :statement "0.2 +"} 
                {:type :call :statement (list "step" "gl_FragCoord.x" "0.4")})}}))}
    {:type :control, :statement (list
        (list "for" 
            {:type :declaration :data-type "int" :name "i" :statement "0" }
            {:type :statement :statement {:type :expression :statement "i < 50"}}
            {:type :assignment :name "i" :statement {:type :expression :statement "i + 1"}})
        {:type :statement :statement {:type :assignment :name "v" :statement "0.3"}})}
    {:type :return :name "v"}]})

(def control-test {:name "control" :function [
    {:type :declaration, :data-type "float", :name "v"}
    {:type :control, :statement (list
        (list "for" 
            {:type :declaration :data-type "int" :name "i" :statement "0" }
            {:type :statement :statement {:type :expression :statement "i < 50"}}
            {:type :assignment :name "i" :statement {:type :expression :statement "i + 1"}})
            {:type :statement :statement {:type :assignment :name "v" :statement "0.3"}})}]})

(def test-functions [test-function control-test])

(def main-function [
    {:type :statement :statement 
        {:type :assignment :name "gl_FragColor" :statement
            {:type :call :statement (list "vec4" 1.0 0.5 0.0 1.0)}}}])

;=========================

(defn get-parsed-parameters [statements]
    (let [params (filter #(= (:type %) :parameter) statements)]
        (str "(" 
             (clojure.string/join ", " 
                (map #(str (:data-type %) " " (:name %)) 
                    params)) 
             ")")))

(defn get-return-type [statements]
    (let [type-statements (filter #(or (= (:type %) :declaration) (= (:type %) :parameter)) 
                                statements)
          returned-variable (:name (first 
                                        (filter #(= (:type %) :return) 
                                            statements)))
          matched-names (filter #(= returned-variable (:name %)) 
                            type-statements)]
        (if (> (count matched-names) 0)
            (:data-type (first matched-names))
            "void")))

;FIXME: Use "recur" instead of tail call
(defn compose-statement [statement]
    (if (list? statement)
        (reduce #(str %1 (compose-statement %2)) "" statement)
        (if (map? statement)
            (case (:type statement)
                :declaration (str (:data-type statement) " " (:name statement) (if (contains? statement :statement) 
                                                                                    (str " = " (compose-statement (:statement statement)))) 
                                ";")
                :assignment (str (:name statement) " = " (compose-statement (:statement statement)))
                :return (str "return" " " (compose-statement (:name statement)) ";")
                :statement (str (compose-statement (:statement statement)) ";")
                :expression (str (compose-statement (:statement statement)))
                :call (str (first (:statement statement)) 
                            "(" (clojure.string/join ", " 
                                    (map compose-statement 
                                        (rest (:statement statement)))) 
                            ")")
                :name (str "<param" (:index statement) ">")
                :control (let [head (first (:statement statement))
                               body (rest (:statement statement))]
                            (str (if (string? head) 
                                     head 
                                     (str (first head) "(" (compose-statement (rest head)) ")"))
                                 (str open-block "\n" (compose-statement body) "\n" close-block)))
                "")
            statement)))

(defn compose-statements [statements]
    (let [valid-types [:assignment 
                       :control 
                       :call 
                       :expression 
                       :statement 
                       :declaration 
                       :return]]
        (map compose-statement 
            (filter (fn [statement] (some #(= (:type statement) %) 
                                        valid-types))
                 statements))))

(defn replace-parameters [glsl-string statements]
    (let [parameters (into [] (filter #(= (:type %) :parameter) 
                                    statements))
         parameter-calls (distinct (re-seq #"<param\d>" glsl-string))
         parameter-indexes (map #(let [num (Integer/parseInt (re-find #"\d" %))] 
                                    (list num (:name (get parameters num))))
                                 parameter-calls)]
        (reduce #(clojure.string/replace %1 
                    (re-pattern (str "<param" (first %2) ">")) 
                    (last %2)) 
            glsl-string parameter-indexes)))

(defn glsl-function [name statements]
    (let [return-type (get-return-type statements)
          parameter-list (get-parsed-parameters statements)
          composed-statements (compose-statements statements)]
        (str return-type " " 
            name 
            parameter-list 
            open-block 
            return-break
            (replace-parameters (apply str composed-statements) 
                statements) 
            return-break 
            close-block 
            return-break)))

(defn declare-functions [functions]
    (apply str (map #(glsl-function (:name %)(:function %))
                     functions)))

(defn glsl-main [statements]
    (glsl-function "main" statements))

(defn assemble-glsl 
    ([glsl-structure]
        (assemble-glsl
            (:functions glsl-structure)
            (:main glsl-structure)))
    ([functions main]
        (str (glsl-prefix)
             (declare-functions functions)
             (glsl-main main))))

(defn write-file [file code]
    (spit (str glsl-folder file) code))