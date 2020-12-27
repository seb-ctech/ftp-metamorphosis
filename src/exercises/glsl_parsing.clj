; 3. Exercise: composing a working glsl piece with two or three layers
(ns exercises.glsl-parsing
    (:require [quil.core :as q :include-macros true]))

(defn setup [])

(def return-break "\n\n")

(defn glsl-prefix []
    (str "#ifdef GL_ES\n" 
         "precision mediump float;\n" 
         "#endif" return-break 
         "uniform vec2 u_resolution;\n" 
         "uniform float u_time;" return-break))

(defn glsl-functions []
    (str ""))

(defn glsl-out [v]
   (str "gl_FragColor = vec4(" v ");"))

(defn glsl-main []
    (str "void main(){" return-break (glsl-out "1, 0, 1, 1") return-break "}"))

(defn assemble-glsl []
    (str (glsl-prefix)(glsl-functions)(glsl-main)))

(spit "src/exercises/glsl/test.frag" (assemble-glsl))