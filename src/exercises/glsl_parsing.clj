; 3. Exercise: composing a working glsl piece with two or three layers
(ns exercises.glsl-parsing
    (:require [quil.core :as q :include-macros true]))

(defn setup [])

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
    {:type :return, :statement "v"}
    {:type :declaration, :data-type "int", :name "v"}
    {:type :control, :statement "if(u_time > 40)"'(
        {:type :statement, :statement "v = 30 +" {:type :name :index 0} "v = else{<nested2>}"}
    {:type :statement, :statement }
    {:type :statement, :statement "v = 20 + <nested1..>"}
    {:type :call, :statement "add(2, 4)"}
    {:type :statement, :statement "v = v + <param1>"}])

(def main-function [
    {:type :statement, :statement "gl_FragColor = <nested1>;"}
    {:type :call, :statement "vec4(1, 1, 1, 1)"}
])

(defn get-parsed-parameters [statements]
    (let [params (filter #(= (:type %) :parameter) statements)]
        (str "(" (clojure.string/join ", " (map #(str (:data-type %) " " (:name %)) params)) ")")))
    

;TODO: Find recursive reduction algorithm to compose complex statements
(defn compose-statements [statements]
    (let [decs (filter #(= (:type %) :declaration) statements)])

(defn get-return-type [statements]
    (let [type-statements (filter #(or (= (:type %) :declaration) (= (:type %) :parameter)) statements)
          returned-variable (:statement (first (filter #(= (:type %) :return) statements)))
          matched-names (filter #(= returned-variable (:name %)) type-statements)]
        (if (> (count matched-names) 0)
            (:data-type (first matched-names))
            "void")))

(defn glsl-function [name statements]
    (let [return-type (get-return-type statements)
          parameter-list (get-parsed-parameters statements)
          composed-statements (compose-statements statements)]
        (str return-type " " name parameter-list open-block
            (apply str compose-statements) close-block)))

(defn glsl-functions [functions]
    (apply str functions))

(defn glsl-main [statements]
    (glsl-function "main" statements))

(defn assemble-glsl []
    (str (glsl-prefix)(glsl-functions [test-function])(glsl-main main-function)))